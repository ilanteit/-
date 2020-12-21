import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class guii {
    private JPanel panel1;
    private JButton startGameButton;
int counter=0;

    public guii() {
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;

                JOptionPane.showMessageDialog(null,"you have been bamboozled");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame=new JFrame("Pokemon game");
        frame.setContentPane(new guii().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



}
