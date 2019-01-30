import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.*;

class showingTable extends JDialog{    //показывает таблицу, задание точек
    JButton btOk,btCancel,jButton,jButtonTest;    JTable Table;
    private final showingTable frame = this;
    public void addToTable(Point point) { //добавление точек в таблицу
        DefaultTableModel dtm = (DefaultTableModel) Table.getModel();
        Object[] values = new Object[2];
        values[0] = point.getX(); values[1] = point.getY();
        dtm.addRow(values);
        Table = new JTable(dtm);    }
    public  showingTable(MenuFrame parent) { // конструктор класса диалогового окна с кнопками и таблицей
        super(parent,true); setSize(300,500);
        setLayout(new FlowLayout());   jButton = new JButton("Добавить точку");
        jButtonTest = new JButton("Пример");  btOk = new JButton("Ввод");
        btCancel = new JButton("Отмена");
        Table = new JTable(1,2);Table.setValueAt("X", 0, 0); Table.setValueAt("Y", 0, 1);
        Table.setGridColor(Color.black);
        add(jButton); add(jButtonTest); add(Table); add(btOk); add(btCancel);
        jButton.addActionListener(new ActionListener()  // новое диалоговое окно для ввода точек
        {
            @Override // переопределение
            public void actionPerformed(ActionEvent e)            {
                DialogWindow dialogWindow = new DialogWindow(frame);
                dialogWindow.setVisible(true);  }        });

        jButtonTest.addActionListener(new ActionListener() //данные с примера
        {
            @Override
            public void actionPerformed(ActionEvent e)            {
                Point[] points = setOfPoints.GetTestExample();
                DefaultTableModel dtm = (DefaultTableModel)Table.getModel();
                for(int i=0; i<points.length; i++) {
                    Object[] obj = new Object[2];
                    obj[0]=points[i].getX(); obj[1]=points[i].getY();
                    dtm.addRow(obj); }
                Table=new JTable(dtm);  }  });

        //данные с таблицы переносятся
        btOk.addActionListener(ac -> {
            Point[] points = new Point[Table.getRowCount() - 1];
            double temp1, temp2;
            for(int i=0; i<points.length; i++) {
                temp1 = Double.valueOf(Table.getValueAt(i+1, 0).toString());
                temp2 = Double.valueOf(Table.getValueAt(i+1, 1).toString());
                points[i]=new Point(temp1,temp2); }
            parent.sets = new setOfPoints(points);
            if(points.length!=0) {
                parent.initXcYc(); parent.printXY();  parent.printPoints(); }
            setVisible(false);   });

        btCancel.addActionListener(new CancelListener());    }

    class CancelListener implements ActionListener{
        public void actionPerformed(ActionEvent ac){ dispose(); setVisible(false); }}

    class DialogWindow extends JDialog { //диалоговое окно для ввода точек
        private JLabel jLabelX = new JLabel("X:");
        private JLabel jLabelY = new JLabel("Y:");
        private JTextField jTextFieldX = new JTextField(5);
        private JTextField jTextFieldY = new JTextField(5);
        private JButton jButtonOK = new JButton("OK");
        private JButton jButtonCancel = new JButton("Отмена");

        public DialogWindow(final showingTable parentFrame) {
            super(parentFrame, "Ввод данных");  this.setSize(200, 200);
            this.setLayout(new FlowLayout());   this.add(jLabelX);
            this.add(jTextFieldX); this.add(jLabelY);  this.add(jTextFieldY);
            this.add(jButtonOK);  this.add(jButtonCancel);

            jButtonCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jTextFieldX.setText("");jTextFieldY.setText(""); setVisible(false);} });

            jButtonOK.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { try {
                    int x = Integer.parseInt(jTextFieldX.getText());
                    int y = Integer.parseInt(jTextFieldY.getText());
                    Point point = new Point(x, y);
                    parentFrame.addToTable(point); setVisible(false);}
                    catch (NumberFormatException ex) {   } }   });    }  }  }

class MenuFrame  extends JFrame { //создание главного окна
    JTable table; JTextField jtf; setOfPoints sets = null; int xc=0; int yc=0;
    JTable jTableResult=new JTable(0,2);
    DefaultTableModel dtm = (DefaultTableModel)jTableResult.getModel();

    protected void initXcYc() { // переносим центр координат
        xc=this.getWidth()/2;  yc=this.getHeight()/2+30; }

    public void printXY() {  //рисуем ось координат
        Graphics g = getGraphics(); g.setColor(Color.BLACK);
        g.drawLine(xc, 60, xc, this.getHeight());
        g.drawLine(0, yc, this.getWidth()-15, yc);
        g.drawLine(xc-5, this.getHeight()-25, xc, this.getHeight()-10);
        g.drawLine(xc+5, this.getHeight()-25, xc, this.getHeight()-10);
        g.drawLine(this.getWidth()-30, yc+5, this.getWidth()-15, yc);
        g.drawLine(this.getWidth()-30, yc-5, this.getWidth()-15, yc);
        printPoint(new Point(0, 0));    }

    public void printPoint(Point point) { //рисуем точку
        Graphics g = getGraphics();
        int x=(int)point.getX(); int y=(int)point.getY();
        g.fillOval(x+xc-3, y+yc-3, 6, 6);
        g.drawString("("+x+", "+y+")", x+xc, y+yc-5);
    }
    public void printCentPoint(Point point) { //рисуем центр окружности
        Graphics g = getGraphics();
        int x=(int)point.getX(); int y=(int)point.getY();
        g.setColor(Color.red);
        g.fillOval(x+xc-5, y+yc-5, 10, 10);
        g.drawString("("+x+", "+y+")", x+xc, y+yc-5);    }

    public void printPoints() { //все точки отображаем
        Graphics g = getGraphics();
        if(sets!=null){
            Point[] points = sets.points;
            for(int i=0; i<points.length; i++) printPoint(points[i]);    }  }

    public void printRes(double x, double y, double radius){  //выводим результат
        Graphics gr = getGraphics(); Graphics2D g = (Graphics2D)gr;
        BasicStroke pen1 = new BasicStroke(5);
        g.setStroke(pen1);    g.setColor(Color.green);
        g.drawOval((int)x+ xc - (int)radius,(int)y + yc -(int)radius,2*(int)radius,2*(int)radius);    }

    public MenuFrame(){ //конструктор главного окна
        super("Лабораторная работа №3"); //Заголовок окна
        JMenuBar  menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu workMenu = new JMenu("Задача");
        this.setSize(600,400);       this.setLocation(500, 100);
        JMenuItem clearwind = new JMenuItem("Очистить");  fileMenu.add(clearwind);
        JMenuItem exitItem = new JMenuItem("Выход");      fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { System.exit(0);} });

        clearwind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {	dtm.setRowCount(0);
                repaint();   }  });

        JMenuItem set = new JMenuItem("Задать точки");    workMenu.add(set);
        JMenuItem solve = new JMenuItem("Решить задачу"); workMenu.add(solve);
        menuBar.add(fileMenu); menuBar.add(workMenu); setJMenuBar(menuBar);

        JPanel jPanel = new JPanel(new FlowLayout());
        jPanel.add(jTableResult); this.setLayout(new BorderLayout());
        this.add(jPanel, BorderLayout.WEST);

        set.addActionListener(new showTable(this));

        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point []circle = sets.SearchMaxArea();
                Point center = sets.Center(circle);
                double radius = sets.distance(center,circle[0]);
                Point[] res = sets.maxPoints(center, radius);
                if(circle!=null) //если окружность такая есть
                {   //выводим таблицу с результатами
                    Object[] obj = new Object[2]; obj[0]="X"; obj[1]="Y";
                    dtm.addRow(obj);
                    for(int i=0; i<sets.max; i++) {
                        obj = new Object[2];       obj[0]=res[i].getX();
                        obj[1]=res[i].getY();      dtm.addRow(obj);    }

                    obj = new Object[2]; obj[0]="Radius"; obj[1]= (int)radius; dtm.addRow(obj);
                    obj = new Object[2]; obj[0]="Max number"; obj[1]= (int) sets.getMAX();
                    dtm.addRow(obj);

                    jTableResult = new JTable(dtm);

                    printRes(center.getX(),center.getY(),radius);
                    printPoints();  printCentPoint(center);    }  }  });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    }

    class showTable implements ActionListener{
        MenuFrame menufr;
        showTable(MenuFrame mf) {
            menufr = mf;
        }

        public void actionPerformed(ActionEvent ae) {
            showingTable d = new showingTable(menufr);
            d.setVisible(true);
        }
    }
}

