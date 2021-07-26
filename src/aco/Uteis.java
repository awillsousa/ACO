
package aco;

import java.util.Random;

public class Uteis 
{
        
    public static int geraNumeroAleatorio(int valorMax)
    {
        long semente;
        
        semente = System.nanoTime();
        
        Random r = new Random();
        r.setSeed(semente);        
        
        return (r.nextInt(valorMax));
    }
    
    public static double geraNumeroAleatorio()
    {
        long semente;
        
        semente = System.nanoTime();
        
        Random r = new Random();
        r.setSeed(semente);        
        
        return (r.nextDouble());
    }

    
    public int[] geraPermutacaoRandomica(int n )
    {
       int  troca, no, totalAtribuido = 0;
       double rnd;
       int[]  r = new int[n];

       for (int i = 0 ; i < n; i++) 
         r[i] = i;

       for (int i = 0 ; i < n ; i++ ) 
       {
         rnd  = Uteis.geraNumeroAleatorio();
         no = (int) (rnd  * (n - totalAtribuido)); 
         
         troca = r[i];
         r[i] = r[i+no];
         r[i+no] = troca;
         totalAtribuido++;
       }
       return (r);
    }
    
    public static double calculaCusto(double matriz[][], int passeio[])
    {
        double custo = 0.0;
        int cidadeAnterior, cidadeAtual;
        double valorMatrix = 0.0;
        int k = 0;
        
        cidadeAnterior = passeio[0];
        for (int i = 1; i < passeio.length; i++)
        {
            cidadeAtual = passeio[i];
            if ((cidadeAnterior == 0)||(cidadeAtual == 0))
                valorMatrix = 0;
            else
                valorMatrix = matriz[cidadeAnterior-1][cidadeAtual-1];
            
            custo = custo + valorMatrix;            
            cidadeAnterior = passeio[i];
        }    
        
        cidadeAtual = passeio[0];
        if ((cidadeAnterior == 0)||(cidadeAtual == 0))
                valorMatrix = 0;
            else
                valorMatrix = matriz[cidadeAnterior-1][cidadeAtual-1];
        //custo = custo + matriz[cidadeAnterior-1][cidadeAtual-1];        
        custo = custo + valorMatrix;        
        
        return (custo);
    }

}
