package net.bar.controller;

import net.bar.model.Database;
import net.bar.view.Monitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;

public class ActionManagement implements ActionListener {
    /*
     JTextField admPasswordInput
    JTextField nameInput
    JTextField priceInput
    JTextField amountInput

    JButton subtractButton
    JButton changeAmountButton
    JButton changePriceButton
    JButton addButton
    JButton removeButton
    JButton totalButton
     */

    private final Monitor monitor = new Monitor();
    private Database database = new Database();

    private final DefaultTableModel dtmCigarettes = new DefaultTableModel(new String[]{"Codice", "Nome", "Prezzo", "Quantità"}, 0);
    private final DefaultTableModel dtmHistory = new DefaultTableModel(new String[]{"Nome", "N° Vendute"}, 0);


    private final String password = "admin";  //password per riferimento
    private String lastInputCode = "";
    private boolean ACCESS = false;
    private boolean CURRENT_ACTION = true;


    public ActionManagement() throws IOException {
        monitor.see();
        action();
        ACCESS = false;
    }

    private void action() {
        open();
        timer();
        monitor.cigaretteTable.setModel(dtmCigarettes);
        monitor.historyTable.setModel(dtmHistory);
        monitor.redoButton.setEnabled(false);

        monitor.choiceActionButtonGroup.add(monitor.editRadioButton);
        monitor.choiceActionButtonGroup.add(monitor.saleRadioButton);
        monitor.editRadioButton.setActionCommand("e");
        monitor.saleRadioButton.setActionCommand("s");
        monitor.editRadioButton.addActionListener(this);
        monitor.saleRadioButton.addActionListener(this);

        monitor.saleRadioButton.doClick();


        monitor.cigaretteTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    int row = monitor.cigaretteTable.getSelectedRow();
                    String code = monitor.cigaretteTable.getValueAt(row, 0).toString();
                    monitor.codeInput.setText(code);
                    if (e.getClickCount() == 2) {
                        subtractAction();

                    }
                    if (monitor.cigaretteTable.isEditing()) {
                        monitor.cigaretteTable.removeEditor();
                    }
                    monitor.cigaretteTable.clearSelection();


                } catch (Exception _) {
                    JOptionPane.showMessageDialog(null, "errore");
                }

            }

        });


        monitor.admPasswordInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                ACCESS = monitor.admPasswordInput.getText().equals(password);
            }
        });

        monitor.priceInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                String txt = monitor.priceInput.getText();
                int i = 0;
                while ((!txt.isEmpty()) && i < txt.length()) {
                    if ((txt.charAt(i) < 48 || txt.charAt(i) > 57) && txt.charAt(i) != '.') {
                        monitor.priceInput.setText("");
                        break;
                    }
                    i++;
                }
            }
        });

        monitor.amountInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                String txt = monitor.amountInput.getText();
                int i = 0;
                while ((!txt.isEmpty()) && i < txt.length()) {
                    if (txt.charAt(i) < 48 || txt.charAt(i) > 57) {
                        monitor.amountInput.setText("");
                        break;
                    }
                    i++;
                }
            }
        });





        monitor.jListButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    switch (monitor.jListButton.getSelectedIndex()) {
                        case 0 -> {  //cambio quantità
                            int newAmount = Integer.parseInt(monitor.amountInput.getText());
                            String code = monitor.codeInput.getText().trim();
                            if (!database.changeAmount(code, newAmount, ACCESS)) {
                                JOptionPane.showMessageDialog(monitor, "Qualcosa è andato storto", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                            } else {
                                updateAllTable(false);  //solo la tabella principale
                                clearAll();
                            }
                        }

                        case 1 -> {  //cambio prezzo
                            float newPrice = Float.parseFloat(monitor.priceInput.getText());
                            String code = monitor.codeInput.getText().trim();
                            if (!database.changePrice(code, newPrice)) {
                                JOptionPane.showMessageDialog(monitor, "Qualcosa è andato storto", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                            } else {
                                updateAllTable(false);  //solo la tabella dello storico
                                clearAll();
                            }

                        }
                        case 2 -> {  //nuovo pacchetto
                            String code = monitor.codeInput.getText().trim();
                            String name = monitor.nameInput.getText().trim();
                            float price = Float.parseFloat(monitor.priceInput.getText());
                            int amount = Integer.parseInt(monitor.amountInput.getText());
                            if (!database.newCigarette(code, name, price, amount)) {
                                JOptionPane.showMessageDialog(monitor, "Sigaretta già in lista", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                            } else {
                                dtmCigarettes.addRow(new String[]{code, name.toUpperCase(), price + "", amount + ""});
                                clearAll();
                            }
                        }
                        case 3 -> {  //rimuovi pacchetto
                            if (ACCESS) {
                                String code = monitor.codeInput.getText().trim();
                                if (!database.removeCigarette(code)) {
                                    JOptionPane.showMessageDialog(monitor, "Sigaretta non trovata", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                                } else {
                                    updateAllTable(false);
                                    clearAll();
                                }
                            }
                        }
                        case 4 -> {  //totale
                            if (ACCESS) {
                                JOptionPane.showMessageDialog(monitor, "il totale lordo ammonta a € : " + database.getGrossReceipts(), "Totale",
                                        JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                                int lenHistory = monitor.historyTable.getRowCount();  //svuotamento tabella history
                                for (int i = 0; i < lenHistory; i++) {
                                    dtmHistory.removeRow(0);
                                }
                                database.getHistory().clear();
                                monitor.redoButton.setEnabled(false);
                                JOptionPane.showMessageDialog(monitor, "TI RICORDO che il totale lordo ammonta a € : " + database.getGrossReceipts() + " ", "Totale",
                                        JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                                database.setGrossReceipts(0); //azzera il lordo
                                clearAll();
                            }
                        }
                    }

                } catch (Exception _) {
                    JOptionPane.showMessageDialog(monitor, "Qualcosa è andato storto", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);

                } finally {
                    monitor.jListButton.clearSelection();
                }

            }
        });


        monitor.redoButton.addActionListener(_ -> {
            try {
                if (!database.redo(lastInputCode)) {    //se è falso
                    JOptionPane.showMessageDialog(monitor, "Nome errato o quantità pari a 0", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
                } else {
                    monitor.redoButton.setEnabled(false);
                    lastInputCode = "";
                    updateAllTable(true);
                }
            } catch (Exception _) {
                JOptionPane.showMessageDialog(monitor, "Qualcosa è andato storto", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
            }


        });


        monitor.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });


    }

    private void clearAll() {
        monitor.codeInput.setText("");
        monitor.nameInput.setText("");
        monitor.priceInput.setText("");
        monitor.amountInput.setText("");
    }     //pulisce tutti i field

    private void updateAllTable(boolean alsoHistoryTable) {
        int len = monitor.cigaretteTable.getRowCount();  //svuotamento tabella sigarette
        for (int i = 0; i < len; i++) {
            dtmCigarettes.removeRow(0);
        }

        database.getCigarettesList().forEach(packsOfCigarettes -> {
            String code = packsOfCigarettes.getCode();
            String name = packsOfCigarettes.getName().toUpperCase();
            String price = packsOfCigarettes.getPrice() + "";
            String amount = packsOfCigarettes.getAmount() + "";
            dtmCigarettes.addRow(new String[]{code, name, price, amount});

        });

        if (alsoHistoryTable) {  //anche la tabella history
            int lenHistory = monitor.historyTable.getRowCount();  //svuotamento tabella history
            for (int i = 0; i < lenHistory; i++) {
                dtmHistory.removeRow(0);
            }

            database.getHistory().forEach((k, v) -> dtmHistory.addRow(new String[]{k, v + ""}));  //riempimento tabella history
        }
    }   //riscrive/pulisce le tabelle

    private void subtractAction() {
        try {
            if (!database.subtract(monitor.codeInput.getText().trim())) {    //se è falso
                JOptionPane.showMessageDialog(monitor, "Nome errato o quantità pari a 0", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
            } else {
                monitor.redoButton.setEnabled(true);
                lastInputCode = monitor.codeInput.getText().trim();
                updateAllTable(true);
            }
        } catch (Exception _) {
            JOptionPane.showMessageDialog(monitor, "Qualcosa è andato storto", "Attenzione", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
        }
    }

    private void open() {
        try (
                FileInputStream databaseFIS = new FileInputStream("catalogue.cgr"); //punta al database.fmw
                ObjectInputStream p = new ObjectInputStream(databaseFIS);
        ) {
            this.database = (Database) p.readObject();  //riassegna il database
            updateAllTable(true);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(monitor, "File not found (open)", "Warning", JOptionPane.WARNING_MESSAGE, monitor.iconJop);

        }
    }    //apertura file e scrittura tabelle

    private void close() {
        try (
                FileOutputStream database = new FileOutputStream("catalogue.cgr");
                ObjectOutputStream d = new ObjectOutputStream(database);
        ) {
            d.writeObject(this.database); //carica tutto il database
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(monitor, "File not found (close)", "Warning", JOptionPane.WARNING_MESSAGE, monitor.iconJop);
        }


    }   //salvataggio file

    private void timer() {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            public void run() {
                close();
            }
        };
        timer.schedule(task1, 0, 120000);  //appena inizia,aspetta 0 secondi e si aggiorna ogni 2 min
    }   //salvataggio automatico

    private void blockComponents(boolean b) {
         /*
        edit ->"e"-> b=true e gli entra false
        sale ->"s"-> b=false e gli entra true
         */

        b = !b;
        monitor.admPasswordInput.setEnabled(b);
        monitor.nameInput.setEnabled(b);
        monitor.priceInput.setEnabled(b);
        monitor.amountInput.setEnabled(b);
        monitor.jListButton.setEnabled(b);


        if (!b) {
            Timer timer = new Timer();
            TimerTask task1 = new TimerTask() {
                public void run() {
                    monitor.codeInput.grabFocus();
                    if (check()) {
                        monitor.redoButton.setEnabled(false);
                        cancel();
                    }else if(!monitor.codeInput.getText().isEmpty()){
                        subtractAction();
                        monitor.codeInput.setText("");
                    }
                }
            };
            timer.schedule(task1, 0, 2000);
        }
    }

    public boolean check() {
        return !CURRENT_ACTION;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        edit ->"e"-> FALSE
        sale ->"s"-> TRUE
         */
        clearAll();
        CURRENT_ACTION = monitor.choiceActionButtonGroup.getSelection().getActionCommand().equalsIgnoreCase("s");
        blockComponents(CURRENT_ACTION);
    }
}
