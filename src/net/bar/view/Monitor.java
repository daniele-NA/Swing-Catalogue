package net.bar.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Monitor extends JFrame {



    public final JPasswordField admPasswordInput = new JPasswordField(15);
    public final JTextField codeInput = new JTextField(15);
    public final JTextField nameInput = new JTextField(15);
    public final JTextField priceInput = new JTextField(15);
    public final JTextField amountInput = new JTextField(15);


    public final JButton redoButton = new JButton();

    public final JList<String> jListButton = new JList<>();

    public final ButtonGroup choiceActionButtonGroup = new ButtonGroup();
    public final JRadioButton saleRadioButton = new JRadioButton("  VENDI   ");
    public final JRadioButton editRadioButton = new JRadioButton("  MODIFICA  ");

    public final JTable cigaretteTable = new JTable();
    public final JTable historyTable = new JTable();

    public final BufferedImage bufferedLogo = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ImmagineFrame.png")));
    public final BufferedImage bufferedLogoJop = ImageIO.read(Objects.requireNonNull(getClass().getResource("/LogoJop.png")));
    public final BufferedImage bufferedLogoRedo = ImageIO.read(Objects.requireNonNull(getClass().getResource("/redoImageResizedGrey.png")));
    public final ImageIcon icon = new ImageIcon(bufferedLogo);
    public final ImageIcon iconJop = new ImageIcon(bufferedLogoJop);
    public final ImageIcon iconRedo = new ImageIcon(bufferedLogoRedo);

    public Monitor() throws IOException {
        super("CATALOGO");
        this.setLayout(new BorderLayout(20, 20));
        this.setIconImage(icon.getImage());
    }

    public void see() {
        Container tableContainer = new Container();
        tableContainer.setLayout(new GridLayout(0, 1));  //0,1 una sopra e una sotto

        JScrollPane cigarettePane = new JScrollPane(cigaretteTable);
        createTable(cigaretteTable, cigarettePane);
        JScrollPane historyPane = new JScrollPane(historyTable);
        historyTable.setEnabled(false);
        createTable(historyTable, historyPane);
        tableContainer.add(cigarettePane);
        tableContainer.add(historyPane);

        final JLabel admPasswordLabel = new JLabel("Password amministratore : ");
        final JLabel codeLabel = new JLabel("code : ");
        final JLabel nameLabel = new JLabel("nome : ");
        final JLabel priceLabel = new JLabel("prezzo : ");
        final JLabel amountLabel = new JLabel("quantità : ");

        createLabel(admPasswordLabel);
        createLabel(codeLabel);
        createLabel(nameLabel);
        createLabel(priceLabel);
        createLabel(amountLabel);

        createTextField(admPasswordInput);
        createTextField(codeInput);
        createTextField(nameInput);
        createTextField(priceInput);
        createTextField(amountInput);

        createRadioButton(saleRadioButton);
        createRadioButton(editRadioButton);

        redoButton.setIcon(iconRedo);
        redoButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement("   -cambia quantità");
        defaultListModel.addElement("   -cambia prezzo");
        defaultListModel.addElement("   -nuovo pacchetto");
        defaultListModel.addElement("   -rimuovi pacchetto");
        defaultListModel.addElement("   -totale");
        jListButton.setModel(defaultListModel);
        jListButton.setPreferredSize(new Dimension(270, 180));
        jListButton.setBackground(Color.LIGHT_GRAY);
        jListButton.setForeground(Color.BLACK);
        jListButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        jListButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        Container commandsContainer = new Container();
        commandsContainer.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 1, 20, 1);
        g.gridx = 0;
        g.gridy = 0;
        commandsContainer.add(admPasswordLabel, g);

        g.gridx = 1;
        g.gridy = 0;
        commandsContainer.add(admPasswordInput, g);

        g.gridx = 0;
        g.gridy = 1;
        commandsContainer.add(codeLabel, g);

        g.gridx = 1;
        g.gridy = 1;
        commandsContainer.add(codeInput, g);

        g.gridx = 0;
        g.gridy = 2;
        commandsContainer.add(nameLabel, g);

        g.gridx = 1;
        g.gridy = 2;
        commandsContainer.add(nameInput, g);

        g.gridx = 0;
        g.gridy = 3;
        commandsContainer.add(priceLabel, g);

        g.gridx = 1;
        g.gridy = 3;
        commandsContainer.add(priceInput, g);

        g.gridx = 0;
        g.gridy = 4;
        commandsContainer.add(amountLabel, g);

        g.gridx = 1;
        g.gridy = 4;
        commandsContainer.add(amountInput, g);

        Container containerRadioButton=new Container();
        containerRadioButton.setLayout(new FlowLayout(FlowLayout.CENTER,20,0));
        containerRadioButton.add(saleRadioButton);
        containerRadioButton.add(editRadioButton);

        g.gridx = 0;
        g.gridy = 5;
        commandsContainer.add(containerRadioButton, g);

        g.gridx = 1;
        g.gridy = 5;
        commandsContainer.add(redoButton, g);


        g.gridx = 1;
        g.gridy = 6;
        commandsContainer.add(jListButton, g);


        this.add(commandsContainer, BorderLayout.LINE_START);
        this.add(tableContainer, BorderLayout.CENTER);
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1300, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createTable(JTable table, JScrollPane p) {
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, dtcr);
        table.setBackground(Color.LIGHT_GRAY);
        table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        table.setForeground(Color.BLACK);
        table.setRowHeight(36);
        table.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
        table.getTableHeader().setEnabled(false);
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        table.getTableHeader().setForeground(Color.YELLOW);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        p.getViewport().setBackground(Color.LIGHT_GRAY);
        p.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        p.getVerticalScrollBar().setBackground(Color.DARK_GRAY);  //setta il colore sotto la ScrollBar
        p.setBackground(Color.DARK_GRAY); //imposta il colore dello spazio rimanente in alto a destra
    }

    private void createRadioButton(JRadioButton b) {
        b.setBackground(Color.DARK_GRAY);
        b.setForeground(Color.YELLOW);
        b.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    }

    private void createLabel(JLabel j) {
        j.setBackground(Color.DARK_GRAY);
        j.setBorder(BorderFactory.createEmptyBorder());  //BORDO TRASPARENTE
        j.setForeground(Color.BLACK);
        j.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
    }

    private void createTextField(JTextField t) {
        t.setBackground(Color.LIGHT_GRAY);
        t.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        t.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    }


}
