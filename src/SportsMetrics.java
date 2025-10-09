import java.awt.*;
import javax.swing.*;


public class SportsMetrics {

    JFrame frame;

    public void CreateGUI()
    {
        frame = new JFrame("CSU Sports Metrics"); // Create a new frame window frame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Once the close is clicked end the program
        frame.setLayout(new BorderLayout());  // set the layout


        //Create the different buttons for each category
        JButton fanButton  = new JButton("Fans");
        JButton CoachButton = new JButton("Coach");
        JButton PlayerButton = new JButton("Players");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Used to move the buttons around the frame

        panel.setBackground(AppColors.background); // Access the AppColors to access different colors for the different sections


        //fanButton.setForeground(AppColors.text); //Use to change the color of the text

        //Add the different buttons to the page
        panel.add(fanButton);
        panel.add(CoachButton);
        panel.add(PlayerButton);
        



        frame.add(panel); // Adds the panels that contain the buttons - Adds the buttons to the screen
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);  //Once the application is opened maximize the window
        frame.setVisible(true); // makes the window visible to the user
    }
    
    public static void main(String[] args) throws Exception {
        SportsMetrics m = new SportsMetrics();

        m.CreateGUI();
    }


}
