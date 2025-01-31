import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.io.IOException;

public class Webbrowser extends JFrame implements ActionListener, HyperlinkListener {

    private JTextField urlField;
    private JEditorPane webPane;
    private JProgressBar progressBar;
    private JTextField wibySearchField;
    private JButton wibyButton;

    public Webbrowser() {
        super("Browser (No Java Script support)");

        // URL field
        urlField = new JTextField();
        urlField.addActionListener(this);

        // Wiby search field
        wibySearchField = new JTextField();
        wibySearchField.addActionListener(e -> searchWiby());

        // Web pane
        webPane = new JEditorPane();
        webPane.setEditable(false);
        webPane.addHyperlinkListener(this);

        // HTML and CSS support
        HTMLEditorKit kit = new HTMLEditorKit();
        webPane.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: sans-serif; }");
        webPane.putClientProperty("HTML.StyleSheet", styleSheet);

        JScrollPane scrollPane = new JScrollPane(webPane);

        // Top panel for URL, Wiby search, Wiby button, and progress bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(urlField, BorderLayout.CENTER);
        topPanel.add(wibySearchField, BorderLayout.WEST);

        wibyButton = new JButton("🏠");
        wibyButton.addActionListener(e -> loadURL("https://www.wiby.me/"));
        topPanel.add(wibyButton, BorderLayout.EAST);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        topPanel.add(progressBar, BorderLayout.SOUTH);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);

        loadURL("https://wiby.me/");
    }

    private void searchWiby() {
        String searchTerm = wibySearchField.getText();
        if (!searchTerm.isEmpty()) {
            loadURL("https://wiby.me/?q=" + searchTerm);
        }
    }

    private void loadURL(String url) {
        SwingUtilities.invokeLater(() -> {
            try {
                progressBar.setVisible(true);
                webPane.setPage(new URL(url));
                urlField.setText(url);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading page: " + e.getMessage());
            } finally {
                progressBar.setVisible(false);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == urlField) {
            loadURL(urlField.getText());
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            loadURL(e.getURL().toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Webbrowser::new);
    }
}
