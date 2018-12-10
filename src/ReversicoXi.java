
import com.sun.javafx.scene.control.skin.IntegerFieldSkin;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.random;


public class ReversicoXi {

    List<List<Celula>> campo;
    List<Coord> posJogaveis;

    //player 1 = branco;
    //player 2 = preto;

    public class Coord {
        int x, y;
        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
    class Celula {
        boolean branco, preto, jogavel;
        Celula () { branco = false; preto = false; jogavel = false; }
        public void setBranco() { branco = true; preto = false; jogavel = false; }
        public void setPreto() { preto = true; branco = false; jogavel = false; }
        public void setJogavel() { preto = false; branco = false; jogavel = true; }
        public void setVazio() { preto = false; branco = false; jogavel = false; }
        public String getCelula() {
            if (branco == preto == jogavel == false)
                return "Vazio";
            else
            if (branco == true && preto == false && jogavel == false)
                return "Branco";
            else
            if (branco == false && preto == true && jogavel == false)
                return "Preto";
            else
            if (branco == false && preto == false && jogavel == true)
                return "Jogavel";
            else
                return "-1";
        }
        public String toString() {
            if (branco == preto == jogavel == false)
                return ".";
            else
            if (branco == true && preto == false && jogavel == false)
                return "1";
            else
            if (branco == false && preto == true && jogavel == false)
                return "2";
            else
            if (branco == false && preto == false && jogavel == true)
                return "x";
            else
                return "-1";
        }

    }



    public boolean insereNoCampo(int x, int y, int player) {

        try {
            if (player == 1)
                campo.get(y).get(x).setBranco();
            else
                campo.get(y).get(x).setPreto();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    public void limpaMarcadoresJogaveis() {
        for (int x = 0; x < 8; x++)
            for (int y =0; y < 8; y++) {
                if (campo.get(y).get(x).getCelula() == "Jogavel")
                    campo.get(y).get(x).setVazio();
            }
    }

    public boolean verificaDirecao(int x, int y, int deslocX, int deslocY, String player, String enemy) throws Exception {

        int desloc;
        if (abs(deslocX) < abs(deslocY))
            desloc = abs(deslocY);
        else desloc = abs(deslocX);

        if (x + deslocX > 7 || x + deslocX < 0
         || y + deslocY > 7 || y + deslocY < 0)
            return false;
        else {
            String res = campo.get(y + deslocY).get(x + deslocX).getCelula();

            if (res == "Vazio" || res == "Jogavel" || (res == player && desloc == 1))
                return false; // encontrou posição vazia, não vai procurar mais nesta direção
            else
            if (res == player && desloc > 1) {
                posJogaveis.add(new Coord(x, y));
                return false; //para não procurar mais nesta direção
            }
            if (res == enemy)
                return true;
        }

        throw new Exception();
    }

    public void verificaDirecoesCardeais(int x, int y, String player, String enemy) {

        //  NW  N  NE         (-1,-1) (-1, 0) (-1, 1)
        //   W      E    ->   ( 0,-1) ( 0, 0) ( 0, 1)
        //  SW  S  SE         ( 1,-1) ( 1, 0) ( 1, 1)

        boolean N = true, NE = true, E = true, SE = true,
                S = true, SW = true, W = true, NW = true;

        try {
            for (int c = 1; c < 8; c++) {
                if (N)
                    N = verificaDirecao(x, y, 0, -c, player, enemy);
                if (NE)
                    NE = verificaDirecao(x, y, c, -c, player, enemy);
                if (E)
                    E = verificaDirecao(x, y, c, 0, player, enemy);
                if (SE)
                    SE = verificaDirecao(x, y, c, c, player, enemy);
                if (S)
                    S = verificaDirecao(x, y, 0, c, player, enemy);
                if (SW)
                    SW = verificaDirecao(x, y, -c, c, player, enemy);
                if (W)
                    W = verificaDirecao(x, y, -c, 0, player, enemy);
                if (NW)
                    NW = verificaDirecao(x, y, -c, -c, player, enemy);
            }
        } catch (Exception e)
        { e.printStackTrace(); }
    }

    public void marcaPosicoesLivres(int paraPlayer) { // um ou dois

        String player = (paraPlayer == 1) ? "Branco" : "Preto";
        String enemy = (paraPlayer == 1) ? "Preto" : "Branco";

        posJogaveis = new ArrayList<Coord>();

        int i=0, j=0;
        for(List<Celula> l: campo) {
            for (Celula s : l) {
                if (s.getCelula() == "Vazio")
                    verificaDirecoesCardeais(j, i, player, enemy);
                j++;
            }
            j = 0;
            i++;
        }

        for(Coord c : posJogaveis) {
            campo.get(c.getY()).get(c.getX()).setJogavel();
        }
    }



    // insert Fazer Jogada here.



    public ReversicoXi() {
        int size = 8 * 8;

        this.campo = new ArrayList<List<Celula>>();
        for(int i = 0; i < 8; i++)
            campo.add( new ArrayList<Celula>());
        for(int i = 0 ; i < 8 ; i++)
            for (int j = 0; j < 8; j++)
                campo.get(i).add( new Celula());

        insereNoCampo(3, 3, 1);
        insereNoCampo(4, 3, 2);
        insereNoCampo(3, 4, 2);
        insereNoCampo(4, 4, 1);



        Scanner sc = new Scanner(System.in);
        int jogador = 1;
        while(true) {
            System.out.println("Jogador " + jogador);
            marcaPosicoesLivres(jogador);
            imprimeCampo(campo);

            Collections.shuffle(posJogaveis);
            Coord jogada = posJogaveis.get(0);
            //efetuarJogada(jogador, jogada.getX(), jogada.getY());
            System.out.println("" + jogada.getX() + jogada.getY());

            limpaMarcadoresJogaveis();

            if(jogador == 1) jogador = 2;
            else jogador = 1;

            sc.nextLine();

        }
    }

    public void imprimeCampo(List<List<Celula>> campo /*ArrayList<String>[][] */) {

        for(List<Celula> i: campo) {
            for (Celula s : i)
                System.out.print(" " + s);
            System.out.println();
        }
    }


    public static void main (String[] args) {
        ReversicoXi reversi = new ReversicoXi();



    }
}