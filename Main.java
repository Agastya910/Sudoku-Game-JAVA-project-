package com.company;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;

class DisplayPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private int DisplayWidth = 557;
    private int DisplayHeight = 580;

    private int ButtonsWidth = 200;
    private final Color LB = new Color(0xD2,0x69,0x1E);
    private final Color DB = new Color(0xA5,0x2A,0x2A);
    private final Color P = new Color(0x80,0, 0x80);
    public DisplayPanel()
    {
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                selectNumber(e.getX(),e.getY());
            }
        });
        this.setLayout(new BorderLayout());

        JPanel pb = new JPanel();
        pb.setPreferredSize(new Dimension(ButtonsWidth,DisplayHeight));
        pb.setBackground(LB);

        FlowLayout FL = new FlowLayout();
        FL.setVgap(55);
        FL.setHgap(100);  //set the flow layout to give  symmetric display
        pb.setLayout(FL);
        SButton EYS = new SButton("Enter Sudoku", "EYS");
        EYS.addActionListener(this);
        pb.add(EYS);
        SButton SES = new SButton("Easy Level", "SES");
        SES.addActionListener(this);
        pb.add(SES);

        SButton SHS = new SButton("Hard Level", "SHS");
        SHS.addActionListener(this);
        pb.add(SHS);
        SButton GBS = new SButton("GOBack One Step", "GBS");
        GBS.addActionListener(this);
        pb.add(GBS);
        SButton STS = new SButton("Solution", "STS");
        STS.addActionListener(this);
        pb.add(STS);
        this.add(pb,BorderLayout.WEST);
    }
    private void selectNumber(int x, int y)
    {
        int NumberPosition[] = {3,63,124,187,248,309,372,433,494}; //number position
        final byte pSNumberY = 19;  //the  spacing for the numbers
        if( x < ButtonsWidth + NumberPosition[0])
            return;
        x -= ButtonsWidth - NumberPosition[0];
        byte count;
        byte Xposition = 0; //the position of the selected box 0 - 8 in X
        for(count = 0; count < 9; count++) //check to find position
        {
            if(x > NumberPosition[count])
                Xposition = count;
        }
        byte Yposition = 0;
        for(count = 0; count < 9; count++) //check to find position
        {
            if(y > NumberPosition[count])
                Yposition = count;
        }
        byte position = (byte) (Xposition + Yposition*9);
        byte Xnumber = 0;
        x -=  NumberPosition[Xposition];
        for(count = 0; count < 3; count++)
        {
            if(x >  pSNumberY*count)
                Xnumber = count;}
        byte Ynumber = 0;
        y -=  NumberPosition[Yposition];
        for(count = 0; count < 3; count++)
        {
            if(y >  pSNumberY*count)
                Ynumber = count;
        }
        byte number = (byte) (Xnumber + Ynumber*3);
        MySudoku.step = Smethods.select(MySudoku.sudoku, number, position, MySudoku.step);
        repaint(ButtonsWidth,0, DisplayWidth,DisplayHeight);
    }
    public Dimension getPreferredSize()  //set the preferred size of display panel
    {
        return new Dimension(DisplayWidth + ButtonsWidth,DisplayHeight);
    }
    protected void paintComponent(Graphics g)
    {
        final byte Foot = 24; //the height of the foot for sudoku
        final byte NumberX = 11;
        final byte NumberY = 54;
        final byte blanksize = 59;
        final byte pNumberX = 4;
        final byte pNumberY = 18;
        final byte pSNumberX = 20;
        final byte pSNumberY = 19;
        final int FootMessageX = 96;
        final int FootMessageY = 574;
        final int FootNumberX = 211;
        final int FootNumberY = 574;
        int BigLines[] = {0, 184, 369, 554, 577};
        int SmallLines[] = {62, 123, 247, 308, 432, 493};
        int NumberPosition[] = {3,63,124,187,248,309,372,433,494};
        Font fontSelected = new Font("SansSerif", Font.ROMAN_BASELINE, 70);
        Font fontFoot = new Font("SansSerif", Font.ROMAN_BASELINE, 20);

        Font fontPencil = new Font("SansSerif", Font.ROMAN_BASELINE, 20);
        super.paintComponent(g);
        g.setColor(DB);
        g.setFont(fontPencil);
        byte count;
        for(count = 0; count < 5; count++)
            g.fillRect(0, BigLines[count], DisplayWidth + ButtonsWidth, 3);
        for(count = 0; count < 6; count++)
            g.drawLine(0, SmallLines[count], DisplayWidth + ButtonsWidth, SmallLines[count]);

        g.fillRect(BigLines[0] + ButtonsWidth , 0, 3, DisplayHeight);
        g.fillRect(BigLines[1] + ButtonsWidth , 0, 3, DisplayHeight - Foot);
        g.fillRect(BigLines[2] + ButtonsWidth , 0, 3, DisplayHeight - Foot);
        g.fillRect(BigLines[3] + ButtonsWidth , 0, 3, DisplayHeight);
        for(count = 0; count < 6; count++)
            g.drawLine(SmallLines[count] + ButtonsWidth, 0, SmallLines[count] + ButtonsWidth, DisplayHeight -Foot);
        //foot text
        g.setFont(fontFoot);
        g.drawString("This is Step        in the Sudoku Solution", FootMessageX + ButtonsWidth, FootMessageY);
        g.drawString(String.valueOf(MySudoku.step), FootNumberX + ButtonsWidth, FootNumberY);
        byte numbercount;
        for(numbercount = 0; numbercount < 81; numbercount++)
        {
            g.setColor(DB);
            byte zeros = 0;
            byte outercount;
            for(outercount = 0; outercount < 3; outercount++)
            {
                for(count = 0; count < 3; count++)
                {
                    byte pencilnumber = MySudoku.sudoku[count + outercount*3 + numbercount*9][ MySudoku.step];
                    if(pencilnumber > 0)
                    {
                        if(pencilnumber < 10)
                        {
                            g.setFont(fontPencil);
                            g.drawString(String.valueOf(pencilnumber ), NumberPosition[numbercount%9] + (count*pSNumberX) + pNumberX + ButtonsWidth, NumberPosition[numbercount/9] + outercount*pSNumberY + pNumberY);
                        }
                        else
                        {
                            g.setFont(fontSelected);
                            g.drawString(String.valueOf(pencilnumber - 10), NumberPosition[numbercount%9] + ButtonsWidth + NumberX, NumberPosition[numbercount/9] + NumberY);
                        }
                    }
                    else
                        zeros += 1;
                }
            }
            if(zeros == 9)
            {
                g.setColor(P);
                g.fillRect(NumberPosition[numbercount%9] + ButtonsWidth, NumberPosition[numbercount/9], blanksize, blanksize);
            }
        }
    }


    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand() == "EYS")
            MySudoku.step = 0;   //nothing is selected
        else if(e.getActionCommand() == "STS")
        {
            Smethods.trysudoku(MySudoku.sudoku, MySudoku.step);
        }
        else if(e.getActionCommand() == "GBS")
        {
            if(MySudoku.step > 0)
                MySudoku.step -= 1;
        }
        else if(e.getActionCommand() == "SES")
        {
            Smethods.trysudoku(MySudoku.sudoku, (byte) 0);
            MySudoku.step = 30;
        }
        else if(e.getActionCommand() == "SHS")
        {
            Smethods.trysudoku(MySudoku.sudoku, (byte) 0);
            MySudoku.step = 10;
        }

        repaint(ButtonsWidth,0, DisplayWidth,DisplayHeight);
    }

}

//    import java.awt.BorderLayout
//        import java.awt.Color;
//        import java.awt.Dimension;
//        import java.awt.image.BufferedImage;
//        import java.io.File;
//        import java.io.IOException;
//
//        import javax.imageio.ImageIO;
//        import javax.swing.JFrame;
//        import javax.swing.SwingUtilities;
class MySudoku
{
    public static byte[][] sudoku = new byte[729][82];

    public static byte step = 0;
    private static final int WindowWidth = 2000;
    private static final int WindowHeight = 1500;

    public static void ShowGUI()
    {
        Smethods.start(sudoku);
        final byte border = 20;
        JFrame f = new JFrame("MySudoku");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("sudoku.png"));
        } catch (IOException e) {
        }
        f.setResizable(false);
        f.setIconImage(image);
        f.setSize(WindowWidth, WindowHeight);
        f.setLocation(0, 0);
        f.setLayout(new BorderLayout());  //north south east west and centre		   

        f.add(new SPanel(new Dimension(WindowWidth,border)),  BorderLayout.NORTH);
        f.add(new SPanel(new Dimension(WindowWidth,border)),  BorderLayout.SOUTH);
        f.add(new SPanel(new Dimension(border,WindowHeight)),   BorderLayout.EAST);
        f.add(new SPanel(new Dimension(0,WindowHeight)),   BorderLayout.WEST);
        DisplayPanel dp =new  DisplayPanel();
        dp.setBackground(Color.GREEN);
        f.add(dp, BorderLayout.CENTER);

        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ShowGUI();
            }
        });
    }

}
//import java.awt.Color;
//        import java.awt.Dimension;
//
//        import javax.swing.BorderFactory;
//        import javax.swing.JButton;
class SButton extends JButton
{
    private static final long serialVersionUID = 1L;
    Color DB = new Color(0x8B,0x00,0x00);
    Color LB = new Color(0xAD,0xD8, 0xE6);
    public SButton(String action, String command)
    {
        super(action);
        this.setBackground(LB);
        this.setForeground(DB);
        this.setBorder(BorderFactory.createBevelBorder(0, DB, DB));
        this.setActionCommand(command);
    }
    public Dimension getPreferredSize()
    {
        return new Dimension(130,30);
    }

    public void addActionListener(DisplayPanel displayPanel) {
    }
}
class Smethods
{
    public static void start(byte[][] sudoku)
    {
        int count = 0;
        for(count = 0; count < 729; count++)
            sudoku[count][0] = (byte) (1 + (count % 9));
    }//end of start. 1-9 in 81 spots

    public static void trysudoku(byte[][] sudoku, byte startstep)
    {
        java.util.Random generator = new java.util.Random(System.currentTimeMillis());
        byte step = startstep;
        int trys = 0;
        do
        {

            trys += 1;
            boolean noblanks = true;
            step = startstep;

            while((step < 81) && (noblanks))
            {
                byte number = (byte) generator.nextInt(9);  // 0 to 8
                byte position = (byte) generator.nextInt(81);  //0 to 80
                step = Smethods.select(sudoku,number,position,step);

                boolean standalone = false;
                do
                {
                    standalone = false;
                    byte count;
                    byte incount;
                    for(count = 0; count < 81; count++)
                    {
                        byte nzeros = 0; //we start with no zero count
                        for(incount = 0; incount < 9; incount++)
                        {
                            if(sudoku[count * 9 + incount][step] == 0)
                                nzeros += 1;
                            else
                                number = (byte) (sudoku[count * 9 + incount][step] - 1);
                            if(nzeros == 9)
                                noblanks = false;
                        }
                        if((nzeros == 8) && (number < 10))
                        {
                            step = Smethods.select(sudoku,number,count,step);
                            standalone = true;
                        }
                    }
                }
                while(standalone);

            }
            MySudoku.step = step;
        }
        while((step != 81) && (trys < 500));

    }

    public static byte select(byte[][] sudoku, byte number, byte position, byte step)
    {
        if((sudoku[position*9 + number][step] == 0) || (sudoku[position*9 + number][step] > 9))
            return step;

        step += 1;
        int count = 0;
        for(count = 0; count < 729; count++)
            sudoku[count][step] = sudoku[count][step - 1];
        for(count = 0; count < 9; count++)
            sudoku[position*9 + count][step] = 0;

        byte row =   (byte) (position/9);
        for(count = 0; count < 9; count++)
            sudoku[row * 81 + count * 9 + number][step] = 0;

        byte column =   (byte) (position%9);
        for(count = 0; count < 9; count++)
            sudoku[column * 9 + count * 81 + number][step] = 0;

        int brow =  (position/27)*243;
        column = (byte) (((position%9)/3)*27);
        byte incount;
        for(incount = 0; incount < 3; incount++)
        {
            for(count = 0; count < 3; count++)
                sudoku[brow + column + count * 9 + incount * 81 + number ][step] = 0;
        }
        sudoku[position*9 + number][step] = (byte) (number + 11);
        return step;
    }

}
//import java.awt.Color;
//        import java.awt.Dimension;
//        import java.awt.Panel;
class SPanel extends Panel
{
    private static final long serialVersionUID = 1L;
    Color LB = new Color(0xAD,0xD8, 0xE6);
    public SPanel(Dimension set)
    {
        super();
        this.setBackground(LB);
        this.setPreferredSize(set);
    }
}

