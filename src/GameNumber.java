/**
 * @author zcq
 * @data 2021/5/25 - 16:06
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GameNumber extends JFrame implements ActionListener {
    public GameNumber() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    Container c;
    JLabel data, valueRight, indexRight, msgLabel;
    //JLabel[] dataMsg1, dataMsg2;
    JButton enterButton, againButton, exitButton;
    JTextField txtData;
    JPanel dataPanel;
    JMenuBar bar;
    JMenu set, help;
    JMenuItem setItem, helpItem, exitItem, aboutItem, pset, phelp;
    JPopupMenu pop;
    int count = 8; //最多只能猜14次，默认猜8次
    int counter = 0;//当前已猜的次数
    String result; //正确的数
    String password = "i love you";
    boolean done = false; //是否猜完
    public GameNumber(String title){
        super(title);
        bar = new JMenuBar();
        set = new JMenu("设置");
        help = new JMenu("帮助");
        setItem = new JMenuItem("猜数次数");
        exitItem = new JMenuItem("退出");
        aboutItem = new JMenuItem("关于");
        helpItem = new JMenuItem("请求作弊");
        pop = new JPopupMenu();
        pset = new JMenuItem("设置猜数次数");
        phelp = new JMenuItem("请求作弊");

        data = new JLabel("已猜数值", JLabel.RIGHT);
        Font font = new Font("幼圆",1,13);
        valueRight = new JLabel("只有数值对的个数", JLabel.CENTER);
        indexRight = new JLabel("数值和位置都对的个数", JLabel.CENTER);
        msgLabel = new JLabel("", JLabel.CENTER);
        msgLabel.setForeground(Color.red);
        msgLabel.setFont(new Font("华文隶书", 1, 30));
        valueRight.setFont(font);
        indexRight.setFont(font);
        data.setFont(font);
        valueRight.setForeground(Color.blue);
        indexRight.setForeground(Color.blue);
        data.setForeground(Color.blue);

        enterButton = new JButton("猜猜看");
        exitButton = new JButton(" 退出 ");
        againButton = new JButton("重新开始");

        txtData = new JTextField(15);
        txtData.setFont(new Font("Arial Black", 1, 20));

        JPanel northPanel = new JPanel(new GridLayout(3,1));
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        p1.add(new JLabel("系统已经产生随机数，请输入要猜的数："));
        p1.add(txtData);
        enterButton.addActionListener(this);
        exitButton.addActionListener(this);
        againButton.addActionListener(this);
        setItem.addActionListener(this);
        helpItem.addActionListener(this);
        pset.addActionListener(this);
        phelp.addActionListener(this);
        aboutItem.addActionListener(this);
        exitItem.addActionListener(this);

        p2.add(enterButton);
        p2.add(againButton);
        p2.add(exitButton);
        p3.setLayout(new GridLayout(1,3));
        p3.add(data);
        p3.add(indexRight);
        p3.add(valueRight);
        northPanel.add(p1);
        northPanel.add(p2);
        northPanel.add(p3);

        set.add(setItem);
        set.add(exitItem);
        help.add(helpItem);
        help.add(aboutItem);

        bar.add(set);
        bar.add(help);
        pop.add(pset);
        pop.add(phelp);
        data.setToolTipText("请求作弊的口令: i love you ");
        this.addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent e){
                if ( e.isPopupTrigger() ) {
                    pop.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        txtData.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == 10) {
                    //回车事件
                    if (txtData.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "请输入一个四位数!");
                        txtData.requestFocus();
                        return;
                    }
                    if (!checkValue()) {
                        JOptionPane.showMessageDialog(null,
                                "只能输入0-9之间四个不相同的数字！请重新输入。");
                        txtData.requestFocus();
                        txtData.selectAll();
                        return;
                    }
                    counter++;
                    parseData();
                    if (counter == count && !done) {
                        JOptionPane.showMessageDialog(null,
                                "真是笨，猜了" + counter + "次都没猜到！正确的数是 " +
                                        result);
                        reset();
                    }
                }
            }
        });
        initStr(); //产生随机数

        c = getContentPane();
        c.add(northPanel, BorderLayout.NORTH);
        dataPanel = new JPanel(new GridLayout(15,1));
        c.add(dataPanel, BorderLayout.CENTER);
        c.add(msgLabel, BorderLayout.SOUTH);
        this.setJMenuBar(bar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(120,50,700,550);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e ){ //按钮和菜单的事件处理
        if ( e.getSource().equals(exitButton) || e.getSource().equals(exitItem)) {
            System.exit(0);
        }
        else if (e.getSource().equals(enterButton)){
            if ( txtData.getText().length() == 0 ){
                JOptionPane.showMessageDialog(this,"请输入一个四位数!");
                txtData.requestFocus();
                return;
            }
            if ( !checkValue()) {
                JOptionPane.showMessageDialog(this,"只能输入0-9之间四个不相同的数字！请重新输入。");
                txtData.requestFocus();
                txtData.selectAll();
                return;
            }
            counter ++;
            parseData();
            if ( counter == count && !done) {
                JOptionPane.showMessageDialog(this,"真是笨，猜了" + counter + "次都没猜到！正确的数是 " + result);
                reset();
            }
        }
        else if (e.getSource().equals(againButton) ){
            reset();
        }
        else if ( e.getSource().equals(setItem) || e.getSource().equals(pset)){
            //设置菜单中的设置猜数次数
            if ( counter != 0 ){
                if (JOptionPane.showConfirmDialog(this, "此操作需要重新开始猜数字，继续吗？") == JOptionPane.YES_OPTION ){
                    showSetDialog(true);
                }
            }
            else{
                showSetDialog(false);
            }
        }
        else if ( e.getSource().equals(helpItem) || e.getSource().equals(phelp)){

            //帮助中的 请求外挂作弊菜单
            showAdmin();
        }
        else if ( e.getSource().equals(aboutItem)){
            //关于菜单
            showAbout();
        }
    }
    //验证文本框内的不能有重复的数字
    public boolean checkValue(){
        String txt = txtData.getText();
        if (txt.length() != 4 ){
            return false;
        }
        try{
            Integer.parseInt(txt);
        }
        catch(Exception e){
            return false;
        }
        char[] chs = txt.toCharArray();
        for (int i = 0; i < chs.length; i ++ ){
            int j = txt.indexOf(chs[i]);
            if (i != j){
                return false;
            }
        }
        return true;
    }
    //关于系统信息提示
    public void showAbout(){
        JOptionPane.showMessageDialog(this, "\n《猜数字游戏》\n版本:  v1.0\n");
    }
    //请求作弊前先必须输入管理员密码
    public void showAdmin(){
        String s;
        s = JOptionPane.showInputDialog(this, "请输入VIP验证码：");
        if ( s == null || s.length() == 0 ) {
            return;
        }
        if ( s.equals(password)){
            JOptionPane.showMessageDialog(this, "作弊无耻,正确的数是: " + result );
        }
        else{
            JOptionPane.showMessageDialog(this, "验证码错误！请联系管理员。");
        }
    }
    //设置猜数的次数
    public void showSetDialog(boolean b){
        String s ;
        s = JOptionPane.showInputDialog(this, "请输入猜数的总次数:(3-15)");
        if ( s == null || s.length() == 0) {
            return;
        }
        try{
            int n = Integer.parseInt(s);
            if ( n > 15 ) {
                JOptionPane.showMessageDialog(this, "无效数值！猜数次数必须在3—15之间。");
                return;
            }
            else{
                count = n;
                JOptionPane.showMessageDialog(this, "设置成功！欢迎继续使用。");
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "非法字符！猜数次数必须是3-15之间的数字。");
            return;
        }
        if ( b )  //需要重新开始
            reset();
    }
    //由系统产生一个四位不相同的随机数
    public void initStr(){
        String str = "";
        int n = 0;
        while ( n < 4 ){
            int d = (int)(Math.random()*10);
            if ( str.indexOf(String.valueOf(d)) == -1 ){
                str += String.valueOf(d);
                n ++;
            }
        }
        result = str;
        //System.out.println("********随机数是： " + result);
    }

    public void reset(){ //重新开始
        txtData.setText("");
        enterButton.setEnabled(true);
        initStr();
        c.remove(dataPanel);
        dataPanel = new JPanel(new GridLayout(15,1));
        c.add(dataPanel, BorderLayout.CENTER);
        counter = 0;
        done = false;
        msgLabel.setText("");
        this.validate(); //刷新界面
        txtData.requestFocus();
    }
    //分析猜的结果
    public void parseData(){
        char[] ch1 = txtData.getText().toCharArray();
        int v1 = 0; //位置对且数值对的个数
        int v2 = 0; //只有数值对的个数
        for ( int i = 0; i < ch1.length; i ++ ){
            int index = result.indexOf(ch1[i]);
            if ( index != -1 ){
                if ( i == index ){
                    v1 ++; //位置对且数值对的个数 加 1
                }
                else{
                    v2 ++; //只有数值对的个数 加 1
                }
            }
        }
        //System.out.println("v1=="+v1+"   v2=="+v2);
        if ( v1 == 4 ){ //全猜中了
            done = true;
            enterButton.setEnabled(false);
            msgLabel.setText("恭喜，猜对了！");
            this.validate();
        }
        else{ //将分析结果显示出来
            JPanel temp = new JPanel(new GridLayout(1,3));
            temp.add(new JLabel(txtData.getText(), JLabel.RIGHT));
            temp.add(new JLabel(String.valueOf(v1), JLabel.CENTER));
            temp.add(new JLabel(String.valueOf(v2), JLabel.CENTER));
            dataPanel.add(temp);
            this.validate(); //刷新界面
            txtData.requestFocus();
        }
    }

    public static void main(String [] args){
        new GameNumber("猜数字游戏");
    }

    private void jbInit() throws Exception {
        this.setResizable(false);
    }
}
