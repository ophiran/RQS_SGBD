import javax.swing.JFrame;
import javax.swing.JLabel;

public class LoadingDialog extends JFrame{
	public LoadingDialog() {
		this.setTitle("Loading caches...");
		this.setSize(400, 100);
		this.setResizable(false);
		this.setLocation(500, 500);
		JLabel label = new JLabel();
        label.setText("Please wait");
        label.setLocation(200,50);
        this.add(label);
	}
}
