package gameClient;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame {

    public Boolean flag = false;
    public String id = null;
    public int scenario_num;
    JPanel jp = new JPanel();
    JLabel jl = new JLabel();
    JTextField jt = new JTextField(30);
    JButton jb = new JButton("Enter id");

    public Start() {
      //  setTitle("Menu");
    //    setVisible(true);
       // setSize(400, 200);
    //    setDefaultCloseOperation(EXIT_ON_CLOSE);
//start1();

start2();

    }
//    private void start1(){
//        jp.add(jt);
//
//
//        jt.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String input = jt.getText();
//                id = input;
//                jl.setText(input);
//                flag = true;
//            }
//        });
//
//        jp.add(jb);
//        jb.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String input = jt.getText();
//                //id = input;
//                jl.setText(input);
//                flag = true;
//
//            }
//        });
//
//        jp.add(jl);
//        add(jp);
//        flag = true;
//
//
//    }

    private void start2() {
         this.id = (String)JOptionPane.showInputDialog(null,
                "enter your ID:");

        if (!flag) {
            Integer[] possibilities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
            Integer s = (Integer) JOptionPane.showInputDialog(null,
                    "Choose Level\n",

                    "Customized Level",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    0);

//If a string was returned, say so.
            if (s != null) {
                this.scenario_num = s;
                return;
            }
////If you're here, the return value was null/empty.
//        setLabel("Come on, finish the sentence!");

        }
    }



        //}
        public static void main (String[]args)
        {
            Start t = new Start();
           // t.start2();
            System.out.println();
        }

}

