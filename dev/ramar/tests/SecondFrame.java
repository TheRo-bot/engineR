import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SecondFrame {
    JFrame secondFrame;
    JLabel label;
    JButton closeBt;
    public SecondFrame(){
        initComponent();
    }
    
    public void initComponent(){
        secondFrame = new JFrame("Frame 2");
        secondFrame.setSize(300, 300);
        secondFrame.setLayout(new BoxLayout(secondFrame.getContentPane(),BoxLayout.PAGE_AXIS));
        
        label = new JLabel("Frame 2");
        secondFrame.add(label);
        
        closeBt = new JButton("Close");
        closeBt.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                
                From oracle doc:
                
                dispose
                public void dispose()
                Releases all of the native screen resources used by this Window, its subcomponents, and all of its owned children. That is, the resources for these Components will be destroyed, any memory they consume will be returned to the OS, and they will be marked as undisplayable.
                The Window and its subcomponents can be made displayable again by rebuilding the native resources with a subsequent call to pack or show. The states of the recreated Window and its subcomponents will be identical to the states of these objects at the point where the Window was disposed (not accounting for additional modifications between those actions).
                Note: When the last displayable window within the Java virtual machine (VM) is disposed of, the VM may terminate. See AWT Threading Issues for more information.
                
                */
                
                secondFrame.dispose();// dispose() in a Window class and JFrame is a subclass of a Window class. So to use dispose() method we need to use secondFrame instance
                
                //to close all JFrame's use System.exit(0);
                
                /*
                
                From Oracle doc:
                
                public static void exit(int status)
                Terminates the currently running Java Virtual Machine. The argument serves as a status code; by convention, a nonzero status code indicates abnormal termination.
                This method calls the exit method in class Runtime. This method never returns normally.
                The call System.exit(n) is effectively equivalent to the call:
                 Runtime.getRuntime().exit(n)
                Parameters:
                status - exit status.
                Throws:
                SecurityException - if a security manager exists and its checkExit method doesn't allow exit with the specified status.
                
                */
                
                //System.exit(0);                // 0 - means execution fine
                                // 1, -1 or any not equal 0 - means something is wrong
            }
        });
        secondFrame.add(closeBt);
        
        secondFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new SecondFrame();
        new SecondFrame();
    }
}