
package aco;

import javax.swing.JOptionPane;

public class ACO 
{

    int iteracoes;
    int numMaxIteracoes;
    int numFormigas;
    int numCidades;
    int tamVizinhanca;
    double[][] matriz;          // matriz de distancias
    double[][] feromonio;      // matriz de ferormonios 
    int[][] menoresVizinhos;
    double[][] infoEscolha;     // combinacao de feromonio e informacoes de busca
    Formiga[] formigas;         // formigas computacionais
    int[]   melhorPercurso;    // melhor percurso obtido ate o momento
    double melhorDistancia = Double.MAX_VALUE;     // distancia do melhor circuito obtido até o momento
    
    double alpha;
    double beta;
    double rho;
        
    int[] menorCircuito;
    
    public ACO()
    {
        super();
    }
    
    /* 
     * Realiza o processamento do algoritmo do ACO     
     * Carrega os arquivos usando a TSPLib
     * Inicializa o numCidades e a matriz de distancia entre as cidades
     * Inicializa as variaveis e demais matrizes e depois busca a solução
     */    
    public void executa() throws Exception 
    {
        
        TSPLib ulysses  = new TSPLib("./files/ulysses22.tsp");
        TSPLib circuito = new TSPLib("./files/ulysses22.opt.tour"); 
        
        ulysses.carregaArquivo();
        circuito.carregaArquivo();
        
        numCidades = circuito.tour.length;
        matriz = ulysses.matriz;        
        
        System.out.println(ulysses.info());
        System.out.println(circuito.info());
        
        /* A PARTIR DESSE PONTO COMECA A RESOLUCAO ATRAVES DO ACS */
        
        // inicializa variaveis e demais dados necessarios
        inicializa();
        
        while (condicaoParada())
        {
            constroiSolucoes();
            //buscaLocal();
            atualizaEstatisticas();
            atualizaFeromonio();
            
            iteracoes++;
        }
        
        exibeResultados();
    }
    
    
    public void inicializa()
    {        
        inicializaParametros(); 
        inicializaMatrizes();
        // inicializa feromonios
        double valorInicial = 0.1;
        valorInicial = 1000.0 / ( (double) numCidades * (double) Uteis.calculaCusto(matriz,geraTourGuloso( )) ) ;
        
        inicializaFeromonio(valorInicial);        
        computaListaVizinhosProximos();
        computaMatrizInfo();
        inicializaFormigas();        
        inicializaEstatisticas();
    }

    private void inicializaMatrizes()
    {
        feromonio   = new double[numCidades][numCidades];
        infoEscolha = new double[numCidades][numCidades];
        menoresVizinhos = new int[numCidades][numCidades];         
        //menoresVizinhos = new int[tamVizinhanca][tamVizinhanca];  
        formigas = new Formiga[numFormigas];
    }

    public void computaListaVizinhosProximos()
    {
        for (int i = 0; i < numCidades; i++)
        {            
            Formiga formiga = new Formiga(numCidades);
            formiga.posiciona(0, i);
            
            for (int j = 1; j < this.tamVizinhanca; j++)
            {
                if (i != j)
                {
                    formiga.melhorMaisProximo(j, matriz);                
                    this.menoresVizinhos[i][j] = formiga.tour[j]; 
                }
            }
        }
    }
    
    public void computaMatrizInfo()
    {
        for (int i = 0 ; i < this.numCidades; i++ ) 
        {
            for (int j = 0 ; j < i ; j++ ) 
            {
                infoEscolha[i][j] = Math.pow(feromonio[i][j],alpha) * Math.pow((1.0 / ((double) matriz[i][j] + 0.1)),beta);
                infoEscolha[j][i] = infoEscolha[i][j];
            }
        }
    }
    
    public void inicializaFormigas()
    {
       for (int i = 0; i < numFormigas; i++)
       {
           formigas[i] = new Formiga(numCidades);
           formigas[i].posicionaAleatorio(i, numCidades);
       }
    }
    
    
    public void inicializaParametros()
    {
        iteracoes = 0;        
        melhorPercurso = new int[numCidades];
        
        //numMaxIteracoes = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a quantidade de iteracoes desejadas"));
        numMaxIteracoes = 100;
       // alpha = Double.parseDouble(JOptionPane.showInputDialog(null, "Digite o valor do parametro alpha (valor sugerido: 1.0)"));
       // beta  = Double.parseDouble(JOptionPane.showInputDialog(null, "Digite o valor do parametro beta (valor sugerido: 2.0"));
      //  rho   = Double.parseDouble(JOptionPane.showInputDialog(null, "Digite o valor do parametro rho  (valor sugerido: 0.5)"));
        alpha = 1.0;
        beta = 2.0;
        
      //  numFormigas = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a quantidade de formigas (valor sugerido: 10)"));
      //  tamVizinhanca = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a quantidade de vizinhos a ser armazenada (valor sugerido: 15)"));
        numFormigas = 50;
        tamVizinhanca = 15;        
    }
    
    public int[] geraTourGuloso( )
    {
        int lPasso;
        Formiga formiga = new Formiga(numCidades);
        
        lPasso = 0; // contador de passos
        formiga.posicionaAleatorio(lPasso, numCidades);
        
        lPasso++;
        while ( lPasso < numCidades-1)
        { 
            formiga.melhorMaisProximo(lPasso, matriz);
            lPasso++;
        }
        
        lPasso = numCidades;
        formiga.tour[lPasso] = formiga.tour[0];
    
//	doisOpt( formiga.tour );  
        return (formiga.tour);
    }
            
    public void inicializaFeromonio(double valorInicial)
    {
        for (int i=0 ; i < feromonio.length ; i++ ) 
        {
            for (int j=0 ; j <= i ; j++ ) 
            {
                feromonio[i][j] = valorInicial;
                feromonio[j][i] = valorInicial;
                infoEscolha[i][j] = valorInicial;
                infoEscolha[j][i] = valorInicial;
            }
        }
    }
        
    public void inicializaEstatisticas()
    {
        
    }
    
    public boolean condicaoParada()
    {
        if (iteracoes > this.numMaxIteracoes)            
            return (false);
        
        return (true);        
    }
    
    public void constroiSolucoes()
    {
        // Zera a lista de cidades visitadas de todas as formigas
        for (int i = 0; i < this.numFormigas; i++)
        {
            formigas[i].zeraVisitadas();
        }
        
        int passo = 0;
        
        while (passo < numCidades)
        {        
            passo++;
            for (int k = 0; k < numFormigas; k++)
            {
                ASRegraDecisao(formigas[k], passo);
            }
        }
        
        for (int k = 0; k < numFormigas; k++)
        {
            formigas[k].tour[numCidades] = formigas[k].tour[0];
            formigas[k].setTamanhoTour(Uteis.calculaCusto(matriz, formigas[k].tour));
        }
    }
    
    public void ASRegraDecisao(Formiga formiga, int passo)
    {
        int c = formiga.tour[passo - 1];
        
        double somaProbabilidades = 0.0;
        double[] selecaoProbabilidade = new double[numCidades];
        
        for (int j = 1; j < numCidades; j++)
        {
            if (formiga.visitadas[j]) 
            {
                selecaoProbabilidade[j] = 0.0;
            }
            else
            {
                selecaoProbabilidade[j] = this.infoEscolha[c][j];
                somaProbabilidades += selecaoProbabilidade[j];
            }
        }
        double r = Uteis.geraNumeroAleatorio();
        r = r*somaProbabilidades;
        int j = 1;
        double p = selecaoProbabilidade[j];
        
        while (p < r)
        {
            j++;
            p += selecaoProbabilidade[j];
        }
        
        formiga.tour[passo] = j;
        formiga.visitadas[j] = true;
                
    }
    
    public void buscaLocal()
    {
        
    }
    
    public void atualizaEstatisticas()
    {
        for (int i = 0; i < numFormigas; i++)
        {
            if (formigas[i].getTamanhoTour() < melhorDistancia)
            {
                melhorPercurso = (int[]) (formigas[i].tour).clone();
                melhorDistancia = formigas[i].getTamanhoTour();
                
            }
        }
    }
    
    public void atualizaFeromonio()
    {
        evaporaFeromonio();
        
        for (int k = 0; k < numFormigas; k++)
        {
            depositaFeromonio();
        }
        this.computaMatrizInfo();
    }
    
    public void evaporaFeromonio()
    {
        for (int i = 0; i < numCidades; i++)
            for (int j = 0; j < numCidades; j++)
            {                
                feromonio[i][j] = (1-rho)*feromonio[i][j];
                feromonio[j][i] = feromonio[i][j];    // matriz simétrica
            }
    }

    public void depositaFeromonio()
    {
        double deltaTao = 1 / melhorDistancia;
        
        for (int i = 0; i < melhorPercurso.length-1; i++)
        {
            int cidadeAtual = melhorPercurso[i];
            int proxCidade  = melhorPercurso[i+1];
            feromonio[cidadeAtual][proxCidade] = (1 - rho) * feromonio[cidadeAtual][proxCidade] + rho*deltaTao;
        }
    }
    
    public void exibeResultados()
    {
        String s = "";
        for (int i = 0; i < melhorPercurso.length; i++)
        {
            s += melhorPercurso[i];
            s += "\n";
        }
        
        JOptionPane.showMessageDialog(null, "Melhor distancia obtida: " + melhorDistancia + "\n" + "Melhor percurso: " + s);
    }
}
