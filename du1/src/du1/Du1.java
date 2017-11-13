/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package du1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author jethro
 */
public class Du1 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.format("Vyberte prosím zobrazení, pro které chcete spočítat"
                + " rovnoběžky a poledníky\n");
        System.out.format("Pro Lambertovo zobrazení stiskněte 'L'\n");
        System.out.format("Pro Marinovo zobrazení stiskněte 'A'\n");
        System.out.format("Pro Mercatorovo zobrazení stiskněte 'M'\n");
        System.out.format("Pro Braunovo zobrazení stiskněte 'B'\n");
        System.out.format("Pro Behrmanovo zobrazení stiskněte 'H'\n");
        char p = readChar();
//      výběr zobrazení
        if (p != 'A'&& p != 'L' && p != 'B' && p != 'M' && p != 'H'){
            System.out.format("Byl zadán chybný znak\n");
            return;
        }
//      podmínka - pokud zadá uživatel něco jiného, než je v nabídce, tak se 
//      program ukončí
        System.out.format("Zvolte prosím celočíselné měřítko. (Číslo za "
                + "dvojtečkou) např: 50000000 \n");
        int scale = readInt();
        System.out.format("Zadejete prosím poloměr Země v km, pokud ho neznáte,"
                + " stiskněte 0\n");
        double r = readDouble();
        if (r == 0){
            r = 6371.11;
        }
//      načtení poloměru Země a následná podmínka, která zajišťuje, že se
//      použije předdefinovaný poloměr, když se stiskne 0
        int nr = 9;
        int np = 18;
        double rov = 0;
        double pol = 0; 
        double f = 0;  
//      vytvoření proměnných, které použiji v metodách
        if (p == 'A'){
            marin(scale,r,nr,np,rov,pol); 
        }
        else if (p == 'L'){
            lamb(scale,r,nr,np,rov,pol);
          }
        else if (p =='B'){
            braun(scale,r,nr,np,rov,pol);
        }
        else if (p == 'M'){
            merc(scale,r,nr,np,rov,pol);
        }
        else if (p == 'H'){
            f = behr(scale,r,nr,np,rov,pol);
            if (f == 100){
                return;
//         ukončí program když se vrátí špatná hodnota viz metoda Behr
//         tato metoda se liší od ostatních a vrací hodnotu sečných rovnoběžek,
//         kterou pak použiji v bonusové metodě na výpočet bodu
            }
        }
//      podmínka, která zavolá metodu podle zvoleného zobrazení
        System.out.format("\n");
        bod(p, scale, r, rov, pol,f);
//      zavolání bonusové metody
    }
//    komentáře, hlavně u první metody  
    public static void marin(int scale, double r,int nr, int np, double rov,
            double pol){
        System.out.format("Rovnoběžky po 10°:");
        for (int i = -9; i <= nr; i++) {
//          cyklus jde od -9 do 9, to vyjadřuje cestu od "pólu k pólu"
            rov = r * (i*10*Math.PI/180);
            podminkarov(scale, rov);
//          zovolá metodu pro výpis rovnoběžek
        }
        poledniky(pol,scale,r,np);
//      zavolá metodu pro výpočet a výpis poledníků
    }
//  Metoda, která vypočte rovnoběžky v Marinově zobrazení a zavolá metodu,
//  která spočte poledníky. V prvním cyklu se zavolá další metoda, kde se
//  nacházý přepočet na cm a podmínka pro výpis pomlčky. Vstupem je scale-
//  měřítko, poloměr Země r, nr - počet rovnoběžek na jedné polokouli, np počet 
//  poledníků na jedné polokouli, proměnná rov a pol - proměnné, do kterých
//  ukládám jednotlivé rovnoběžky a poledníky. Metoda je typu void. Další
//  metody pro zobrazení fungují na stejném principu.  
    public static void lamb(int scale, double r,int nr, int np, double rov, 
            double pol){
        System.out.format("Rovnoběžky po 10°:");
        for (int i = -9; i <= nr; i++) {
            rov = r * Math.sin((10*i*Math.PI/180));
            podminkarov(scale, rov);
        }
        poledniky(pol,scale,r,np); 
    }
    public static void braun(int scale, double r,int nr, int np, double rov, 
            double pol){
        System.out.format("Rovnoběžky po 10°:");
        for (int i = -9; i <= nr; i++) {
            rov = 2 * r * Math.tan(((i*10*Math.PI/180)/2));
            podminkarov(scale, rov);
        }
        poledniky(pol,scale,r,np);
    }    
    public static void merc(int scale, double r,int nr, int np, double rov, 
            double pol){
        int d;
//      tady jsem si vytvořil další proměnnou, která bude symbolizovat
//      zbytek do 90 
        System.out.format("Rovnoběžky po 10°:");
        for (int i = -8; i <= (nr-1); i++) {
            if (i >= 0){
                d = 90 - i*10;
                rov = r * Math.log((1/Math.tan(((d*Math.PI/180)/2))));
            }
            else {
                d = i*10 + 90;
                rov = r * Math.log((1/Math.tan(((d*Math.PI/180)/2))));
                rov = - rov;
            }
//          podmínka, která ošetřuje mínus, které by logaritmus nevzal a hodil
//          by error
            podminkarov(scale, rov);
        }
        poledniky(pol,scale,r,np);
    }
    public static double behr(int scale, double r,int nr, int np, double rov, 
            double pol) throws IOException{
        double f;
        System.out.format("Chcete sám nadefinovat sečné rovnoběžky?\n");
        System.out.format("Stiskněte 'A' pro ano a 'N' pro ne -> program "
                + "použij již předdefinovanou hodnotu\n");
        char a = readChar();
        if (a == 'A'){
            System.out.format("Zadejte šířku sečných rovnoběžek ve stupních \n");
            double u = readDouble();
            f = u;
            if(f > 90 || f < -90){
                System.out.format("Byla zadána chybná hodnota\n");
                return 100;
//              program se ukončí přes podmínku v mainu
            }
        }
        else if (a == 'N'){
            f = 30;
        }
        else {
            System.out.format("Byl zadán chybný znak\n");
            return 100;
//          vrací 100 -> program se ukončí přes podmínku v mainu
        }
//          podmínka, která použije f0 = 30 pro sečné rovnoběžky, když uživatel 
//          nechce nadefinovat šířku sečných rovnoběžek
        System.out.format("Rovnoběžky po 10°:");
        for (int i = -9; i <= nr; i++) {
            rov = r * Math.sin((i*10*Math.PI/180)) * (1/Math.cos(f*Math.PI/180));
            podminkarov(scale, rov);
        }
        System.out.format("\nPoledníky po 10°:");
        for (int i = -18; i <= np; i++) {
            pol = r * (i*10*Math.PI/180) * Math.cos(f*Math.PI/180);
            podminkapol(scale, pol);
        }
        return f;
//   vrátí hodnotu sečných rovnoběžek, kterou následně použiji pro výpočet bodu
    }
//    Bonusová metoda, kde jsem ještě přidal výjimku, jelikož se ptám na sečné
//    rovnoběžky
    
    public static void bod(char p, int scale, double r, double rov, double pol,
            double f)
            throws IOException
    {
        for(int i = 0;;i++){
            System.out.format("Zadejte zeměpisnou šířku a zeměpisnou délku "
               + "bodu, ve stupních, pro který chcete vypočítat souřadnice na"
               + " papíře. Po zadání bodu (0,0) se program ukončí\n");
            double zs = readDouble();
            double zd = readDouble();
            if (zs == 0 && zd == 0)
            {
                System.out.format("Program bude ukončen\n");
                return;
            }
            else if (zs > 90 || zs < -90 || zd > 90 || zd < -90){
                System.out.format("Zadány chybné souřadnice\n");
                return;
            }
            if (p == 'A'){
                System.out.format("souřadnice y:");
                rov = r * (Math.PI*zs/180);
                podminkarov(scale, rov);
                System.out.format(" souřadnice x:");    
                pol = r * (zd*Math.PI/180);
                podminkapol(scale, pol);
                System.out.format("\n");
            }
            else if (p == 'L'){
                System.out.format("souřadnice y:");
                rov = r * Math.sin((zs*Math.PI/180));
                podminkarov(scale, rov);
                System.out.format(" souřadnice x:");    
                pol = r * (zd*Math.PI/180);
                podminkapol(scale, pol); 
                System.out.format("\n");
            }
            else if (p =='B'){
                System.out.format("souřadnice y:");
                rov = 2 * r * Math.tan(((zs*Math.PI/180)/2));
                podminkarov(scale, rov);
                System.out.format(" souřadnice x:");    
                pol = r * (zd*Math.PI/180);
                podminkapol(scale, pol);
                System.out.format("\n");
            }
            else if (p == 'M'){
                double d;
                if (zs >= 0){
                    d = 90 - zs;
                    rov = r * Math.log((1/Math.tan(((d*Math.PI/180)/2))));
                }
                else {
                    d = zs + 90;
                    rov = r * Math.log((1/Math.tan(((d*Math.PI/180)/2))));
                    rov = - rov;
                }
                System.out.format("souřadnice y:");
                podminkarov(scale, rov);
                System.out.format(" souřadnice x:");    
                pol = r * (zd*Math.PI/180);
                podminkapol(scale, pol); 
                System.out.format("\n");
            }
            else if (p == 'H'){
                System.out.format("souřadnice y:");
                rov = r*Math.sin((zs*Math.PI/180))*(1/Math.cos(f*Math.PI/180));
                podminkarov(scale, rov);
                System.out.format(" souřadnice x:");    
                pol = r * (zd*Math.PI/180) * Math.cos(f*Math.PI/180);
                podminkapol(scale, pol);
                System.out.format("\n");
            }
//          vše je zabaleno do cyklu, aby se program stále ptal uživatele
        }
    }
//    Bonusová metoda pro výpočet bodu. Přes podmínku zjistím, jaké zobrazení
//    se počítalo a následně použiji pro výpočet ty samé vzorce a metody.
     
    public static void poledniky(double pol, int scale, double r, int np){
        System.out.format("\nPoledníky po 10°:");
        for (int i = -18; i <= np; i++) {
//          cyklus jde od -18 do 18, to vyjadřuje cestu "po rovníku"
            pol = r * (i*10*Math.PI/180);
            podminkapol(scale, pol);
        }
    }
//  Metoda pro výpočet poledníků, která volá metodu pro jejich výpis
    public static void podminkapol(int scale, double pol){
        pol = (pol/scale) * 100000;
//      přepočet na cm
        if (pol <= 100 && pol >= -100){
            System.out.format("  %.1f",pol);
        }
        else {
            System.out.format("  -");
        }
//      podmínka, která jednoduše vypisuje hodnoty, které nejsou větší než 1 m
    }
//  Metoda, která vypisuje poledníky a přepočítává je na cm
    public static void podminkarov(int scale,double rov){
        rov = (rov/scale) * 100000;
        if (rov <= 100 && rov >= -100){
            System.out.format("  %.1f",rov);
        }
        else {
            System.out.format("  -");
        }
    }
//  Metoda, která vypisuje rovnoběžky
    
    public static int readInt() throws IOException{
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }
    
    public static double readDouble() throws IOException{
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(System.in));
        return Double.parseDouble(reader.readLine());
    }
    
    public static char readChar() throws IOException{
        Scanner s= new Scanner(System.in);
        return  s.next().charAt(0);
    }
}
