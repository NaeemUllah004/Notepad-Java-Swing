import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notepad extends JFrame implements ActionListener {

    // Main Components
    JTextArea textArea;
    JLabel statusLabel;

    JFileChooser fileChooser;
    File currentFile;

    boolean darkMode = true;
    int fontSize = 16;

    // Menu Items
    JMenuItem newFile, open, save, saveAs, print, exit;
    JMenuItem cut, copy, paste, undo, redo, find, replace, selectAll;
    JMenuItem zoomIn, zoomOut, darkTheme, lightTheme, statusBar;
    JMenuItem insertDate;
    JMenuItem about;
	


    JToolBar toolBar;

    public Notepad() {

        setTitle("Smart Notepad Pro");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Text Area
        textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(textArea);
        add(scroll);

        // File Chooser
        fileChooser = new JFileChooser();

        // Status Bar
        statusLabel = new JLabel(" Ready");
        statusLabel.setBorder(new EtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);

        // Toolbar
        createToolBar();

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Listeners
        textArea.addCaretListener(e -> updateStatus());

        applyDarkTheme();

        setVisible(true);
    }

    // ================= MENU ==================

    private JMenuBar createMenuBar() {

        JMenuBar bar = new JMenuBar();

        // FILE
        JMenu file = new JMenu("File");
        newFile = item("New", "ctrl N");
        open = item("Open", "ctrl O");
        save = item("Save", "ctrl S");
        saveAs = item("Save As", null);
        print = item("Print", "ctrl P");
        exit = item("Exit", null);

        file.add(newFile);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(print);
        file.addSeparator();
        file.add(exit);
		
		//Courses
		
		JMenuItem physics,che,bio,urd;
		JMenu m= new JMenu("courses");
		   physics=item("physics",null);
		    che=item("che",null);
			 bio=item("bio",null);
			  urd=item("urd",null);
		m.add(physics);
		m.add(che);
		m.add(bio);
		m.add(urd);

        // EDIT
        JMenu edit = new JMenu("Edit");

        cut = item("Cut", "ctrl X");
        copy = item("Copy", "ctrl C");
        paste = item("Paste", "ctrl V");
        undo = item("Undo", "ctrl Z");
        redo = item("Redo", "ctrl Y");
        find = item("Find", "ctrl F");
        replace = item("Replace", "ctrl H");
        selectAll = item("Select All", "ctrl A");

        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.addSeparator();
        edit.add(undo);
        edit.add(redo);
        edit.addSeparator();
        edit.add(find);
        edit.add(replace);
        edit.addSeparator();
        edit.add(selectAll);

        // VIEW
        JMenu view = new JMenu("View");

        zoomIn = item("Zoom In", "ctrl +");
        zoomOut = item("Zoom Out", "ctrl -");
        darkTheme = item("Dark Theme", null);
        lightTheme = item("Light Theme", null);
        statusBar = item("Toggle Status Bar", null);

        view.add(zoomIn);
        view.add(zoomOut);
        view.addSeparator();
        view.add(darkTheme);
        view.add(lightTheme);
        view.addSeparator();
        view.add(statusBar);

        // TOOLS
        JMenu tools = new JMenu("Tools");

        insertDate = item("Insert Date/Time", "F5");

        tools.add(insertDate);

        // HELP
        JMenu help = new JMenu("Help");

        about = item("About", null);

        help.add(about);

        bar.add(file);
        bar.add(edit);
        bar.add(view);
        bar.add(tools);
        bar.add(help);
		bar.add(m);

        return bar;
    }

    private JMenuItem item(String name, String key) {

        JMenuItem i = new JMenuItem(name);
        i.addActionListener(this);

        if (key != null)
            i.setAccelerator(KeyStroke.getKeyStroke(key));

        return i;
    }

    // ================= TOOLBAR ==================

    private void createToolBar() {

        toolBar = new JToolBar();

        addTool("New");
        addTool("Open");
        addTool("Save");
        addTool("Cut");
        addTool("Copy");
        addTool("Paste");
        addTool("Find");

        add(toolBar, BorderLayout.NORTH);
    }

    private void addTool(String name) {

        JButton btn = new JButton(name);
        btn.addActionListener(this);
        btn.setFocusPainted(false);

        toolBar.add(btn);
    }

    // ================= ACTIONS ==================

    @Override
    public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();

        try {

            switch (cmd) {

                case "New":
                    textArea.setText("Wohoo! You have Open a New fresh file");
                    currentFile = null;
                    setTitle("Smart Notepad Pro");
                    break;

                case "Open":
                    openFile();
                    break;

                case "Save":
                    saveFile();
                    break;

                case "Save As":
                    saveAs();
                    break;

                case "Print":
                    textArea.print();
                    break;

                case "Exit":
                    System.exit(0);
                    break;

                case "Cut":
                    textArea.cut();
                    break;

                case "Copy":
                    textArea.copy();
                    break;

                case "Paste":
                    textArea.paste();
                    break;

                case "Find":
                    findText();
                    break;

                case "Replace":
                    replaceText();
                    break;

                case "Zoom In":
                    fontSize += 2;
                    updateFont();
                    break;

                case "Zoom Out":
                    fontSize -= 2;
                    updateFont();
                    break;

                case "Dark Theme":
                    applyDarkTheme();
                    break;

                case "Light Theme":
                    applyLightTheme();
                    break;

                case "Toggle Status Bar":
                    statusLabel.setVisible(!statusLabel.isVisible());
                    break;

                case "Insert Date/Time":
                    insertDateTime();
                    break;

                case "About":
                    showAbout();
                    break;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ================= FUNCTIONS ==================

    private void openFile() throws Exception {

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            currentFile = fileChooser.getSelectedFile();

            BufferedReader br = new BufferedReader(new FileReader(currentFile));

            textArea.setText("");

            String line;
            while ((line = br.readLine()) != null)
                textArea.append(line + "\n");

            br.close();

            setTitle(currentFile.getName());
        }
    }

    private void saveFile() throws Exception {

        if (currentFile == null)
            saveAs();
        else
            writeFile(currentFile);
    }

    private void saveAs() throws Exception {

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            currentFile = fileChooser.getSelectedFile();
            writeFile(currentFile);
        }
    }

    private void writeFile(File f) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(textArea.getText());
        bw.close();

        setTitle(f.getName());
    }

    private void updateFont() {

        textArea.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
    }

    private void insertDateTime() {

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        textArea.insert(date, textArea.getCaretPosition());
    }

    private void showAbout() {

        JOptionPane.showMessageDialog(this,
                "Smart Notepad Pro\n" +
                "Developed in Java Swing\n" +
                "For Academic Project\n\n" +
                "© 2026",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStatus() {

        String text = textArea.getText();

        int words = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
        int chars = text.length();
        int lines = textArea.getLineCount();

        statusLabel.setText(" Words: " + words +
                " | Characters: " + chars +
                " | Lines: " + lines);
    }

    // ================= FIND & REPLACE ==================

    private void findText() {

        String find = JOptionPane.showInputDialog("Find:");

        if (find == null || find.isEmpty()) return;

        String content = textArea.getText();

        int index = content.indexOf(find);

        if (index >= 0) {

            textArea.select(index, index + find.length());

        } else {

            JOptionPane.showMessageDialog(this, "Text Not Found");
        }
    }

    private void replaceText() {

        JTextField find = new JTextField();
        JTextField replace = new JTextField();

        Object[] msg = {
                "Find:", find,
                "Replace With:", replace
        };

        int option = JOptionPane.showConfirmDialog(this, msg, "Replace", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {

            textArea.setText(
                    textArea.getText().replace(find.getText(), replace.getText())
            );
        }
    }

    // ================= THEMES ==================

    private void applyDarkTheme() {

        darkMode = true;

        textArea.setBackground(new Color(25, 25, 25));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);

        statusLabel.setBackground(new Color(40, 40, 40));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);

        toolBar.setBackground(new Color(35, 35, 35));
    }

    private void applyLightTheme() {

        darkMode = false;

        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.BLACK);

        statusLabel.setBackground(Color.LIGHT_GRAY);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setOpaque(true);

        toolBar.setBackground(Color.WHITE);
    }

    // ================= MAIN ==================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Notepad();
        });
    }
}
