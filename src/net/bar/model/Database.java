package net.bar.model;

import java.io.Serializable;
import java.util.*;

public class Database implements Serializable {
    private float grossReceipts;   // incasso lordo
    private final List<PackOfCigarettes> cigarettesList = new ArrayList<>();
    private final Map<String, Integer> history = new HashMap<>();

    public boolean newCigarette(String code,String name, float price, int amount) {
        if (search(code) == -1) {   //se non esiste
            cigarettesList.add(new PackOfCigarettes(code,name, price, amount));
            return true;
        }
        return false;
    }

    public boolean subtract(String code) {
        int ndx = search(code);
        if (ndx != -1 && cigarettesList.get(ndx).getAmount() > 0) {
            int amount = cigarettesList.get(ndx).getAmount();
            cigarettesList.get(ndx).setAmount(amount - 1);
            grossReceipts += cigarettesList.get(ndx).getPrice();  //aggiornamento lordo

            String nameOfPack = cigarettesList.get(ndx).getName();  //aggiornamento storico
            if (history.containsKey(nameOfPack)) {
                int amountSubtract = history.get(nameOfPack);
                history.put(nameOfPack, amountSubtract - 1);
            }else{
                history.put(nameOfPack,-1);
            }
            return true;
        }else {
            return false;
        }

    }

    public boolean changeAmount(String code,int newAmount,boolean adm){
        int ndx=search(code);
        if(ndx!=-1){
            if(adm){
                cigarettesList.get(ndx).setAmount(newAmount);   //lo fa indipendentemente
                return true;
            }else{
                int oldAmount=cigarettesList.get(ndx).getAmount();
                if(newAmount>oldAmount){
                    cigarettesList.get(ndx).setAmount(newAmount);
                    return true;
                }
                return false;
            }

        }
        return false;
    }

    public boolean changePrice(String code,float newPrice){
        int ndx=search(code);
        if(ndx!=-1){
            cigarettesList.get(ndx).setPrice(newPrice);
            return true;
        }
        return false;
    }

    public boolean removeCigarette(String code){
        Iterator<PackOfCigarettes> iterator=cigarettesList.iterator();

        while (iterator.hasNext()){
            String codeIterator=iterator.next().getCode();
            if(codeIterator.equalsIgnoreCase(code.trim())){
                iterator.remove();
                return true;
            }

        }
        return false;
    }

    public boolean redo(String code){
        int ndx = search(code);
        if (ndx != -1) {
            int amount = cigarettesList.get(ndx).getAmount();
            cigarettesList.get(ndx).setAmount(amount + 1);
            grossReceipts -= cigarettesList.get(ndx).getPrice();  //aggiornamento lordo

            String nameOfPack = cigarettesList.get(ndx).getName();  //aggiornamento storico
            if (history.containsKey(nameOfPack)) {
                int amountSubtract = history.get(nameOfPack);
                history.put(nameOfPack, amountSubtract + 1);
                if(history.get(nameOfPack)==0){   //dopo che è stata aggiornata
                    history.remove(nameOfPack,0);
                }
            }
            return true;
        }
        return false;

    }


    private int search(String code) {
        for (int i = 0; i < cigarettesList.size(); i++) {
            if (cigarettesList.get(i).getCode().equalsIgnoreCase(code.trim())) {
                return i;
            }
        }
        return -1;
    }

    public float getGrossReceipts() {
        return grossReceipts;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public List<PackOfCigarettes> getCigarettesList() {
        return cigarettesList;
    }

    public void setGrossReceipts(float grossReceipts) {
        this.grossReceipts = grossReceipts;
    }
}
