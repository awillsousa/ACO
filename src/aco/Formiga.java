
package aco;

public class Formiga 
{
    private double tamanhoTour;    // tamanho do circuito efetuado pela formiga
    public int[] tour;             // circuito efetuado pela formiga
    public boolean[] visitadas;    // vetor das cidades visitadas pela formiga 

    public Formiga(int numCidades)
    {
        tamanhoTour = -1.0;
        tour = new int[numCidades+1];
        visitadas = new boolean[numCidades];
        this.zeraVisitadas();
    }

    public void setTamanhoTour(double tamanhoCircuito)
    {
        this.tamanhoTour = tamanhoCircuito;
    }
    
    public double getTamanhoTour()
    {
        return (this.tamanhoTour);
    }
    
    // Procura a melhor formiga da iteracao corrente
    public int procuraMelhor(Formiga[] formigas) 
    {
        double   menorCircuito;
           int   posMinimo;

        menorCircuito = formigas[0].tamanhoTour;
        posMinimo = 0;
        for( int k = 1 ; k < formigas.length ; k++ ) {
            if( formigas[k].tamanhoTour < menorCircuito ) {
                menorCircuito = formigas[k].tamanhoTour;
                posMinimo = k;
            }
        }
        return (posMinimo);
    }
    
    public void posiciona(int passo, int posicao)
    {
        this.tour[passo] = posicao;
        this.visitadas[posicao] = true;
    }
    
    public void posicionaAleatorio(int passo, int valorMax)
    {
        int posicaoAleatoria;
        
        posicaoAleatoria = Uteis.geraNumeroAleatorio(valorMax-1);
        this.tour[passo] = posicaoAleatoria;
        this.visitadas[posicaoAleatoria] = true;
        
    }

    public void melhorMaisProximo(int passo, double[][] distancias)
    {
        int cidade, cidadeAtual, proxCidade;
        double distanciaMinima;
        int indCidadeAtual;
        proxCidade = visitadas.length;       
    
        cidadeAtual = this.tour[passo - 1];
        distanciaMinima = Double.MAX_VALUE;
        
        indCidadeAtual = cidadeAtual==0?0:cidadeAtual-1;
        this.visitadas[indCidadeAtual] = true;
        //this.visitadas[cidadeAtual-1] = true;  -- alterado para evitar erro de indice na matriz
        
        for ( cidade = 0 ; cidade < visitadas.length; cidade++ ) 
        {
            if ((!this.visitadas[cidade]) /*&& ((cidadeAtual-1) != cidade)*/)
	    {
                if ( distancias[indCidadeAtual][cidade] < distanciaMinima) 
                {
                    proxCidade = cidade;
                    distanciaMinima = distancias[indCidadeAtual][proxCidade];
                }
            } 
        }    
        
        this.tour[passo] = proxCidade+1;
        this.visitadas[proxCidade] = true;        
    }
    
    public void zeraVisitadas()
    {
        for (int i=0; i < visitadas.length; i++)
        {
            this.visitadas[i] = false;
        }
    }    
}