/*
 * Essa classe realiza a carga de um arquivo da TSPLib para uma matriz de distancias entres cidades do problema
 * do Traveling Salesman Problem (Problema do Caixeiro Viajante)
 * 
 */

package aco;
import java.io.*;


/**
 *
 * @author Willian Sousa
 */
public class TSPLib 
{
    
    private static final double PI  = 3.141592;
    private static final double RRR = 6378.388; 
    
    private enum TipoTSP          { TSP, ATSP, SOP, HCP, CVRP, TOUR }
    private enum TipoWeight       { EXPLICIT, EUC_2D, EUC_3D, MAX_2D, MAX_3D, MAN_2D, MAN_3D, CEIL_2D, GEO, ATT, XRAY1, XRAY2, SPECIAL }
    private enum TipoWeightFormat { FUNCTION, FULL_MATRIX, UPPER_ROW, LOWER_ROW, UPPER_DIAG_ROW, LOWER_DIAG_ROW, UPPER_COL, LOWER_COL, UPPER_DIAG_COL, LOWER_DIAG_COL }
    private enum TipoDataFormat   { EDGE_LIST, ADJ_LIST }
    private enum TipoNodeCoord    { TWOD_COORDS, THREED_COORD, NO_COORDS}
    private enum TipoDisplayData  { COORD_DISPLAY, TWOD_DISPLAY, NO_DISPLAY }

    private TipoTSP          tipo;
    private TipoWeight       tipoWeight;
    private TipoWeightFormat tipoWeightFormat;
    private TipoDataFormat   dataFormat;
    private TipoNodeCoord    nodeCoord;
    private TipoDisplayData  displayData;
        
    private String arquivo;              //caminho do arquivo a ser lido
    
    private int     posicao;             // variavel utilizada para verificar a posicao de um caracter dentro de uma cadeia

    private String  palavraChave,        // parametro passado em cada linha do arquivo
                    valor,               // aux do parametro                     
                    linha;               // linha lida do arquivo
    private String  nome,                // identifica o nome do arquivo lido                       
                    comentario;
    private int     dimensao,            // variavel para indicar a dimensao da matriz contendo as distancia lidas do arquivo
                    capacidade;
                    
    public double matriz[][];           // matrix
    public int    tour[];               // guarda o circuito definido em arquivo
    private double coordenadas[][];     //matriz (dimensaox3) matriz[i] - cidades coordenadas[i][0],coordenadas[i][1],coordenadas[i][2] - x,y,z
    
    private boolean continua = true;
    
    public TSPLib(String nomeArquivo)
    {
        this.arquivo = nomeArquivo;
    }
    
    public void carregaArquivo() throws Exception
    { 
        try
	{
            // abre o arquivo para leitura
            FileInputStream fstream = new FileInputStream(this.arquivo);            	    
            BufferedReader in   = new BufferedReader(new InputStreamReader(fstream));


            // Continua lendo linha do arquivo enquanto tiverem linhas para serem lidas
            while (((linha = in.readLine()) != null) && (continua))
	    {
                
                posicao = linha.indexOf(":");
                
                if (posicao != -1)
                {
                    palavraChave = linha.substring(0, posicao);
                    palavraChave = palavraChave.trim();
                    //valor = linha.substring(posicao+1, linha.length() - posicao);
                    valor = linha.substring(posicao+1);
                    valor = valor.trim();
                }
                else
                {
                    palavraChave = String.copyValueOf(linha.toCharArray());
                }
                
                if (palavraChave.equals("NAME"))
                {
                   nome = valor;
                }
                else if (palavraChave.equals("EOF"))
                {
                    continua = false;
                }
                else if(palavraChave.equals("TYPE")) 
                {
                  
                  if(valor.equals("ATSP")) 
                    tipo = TipoTSP.ATSP;
                  else if(valor.equals("TSP"))
                    tipo = TipoTSP.TSP;
                  else if(valor.equals("SOP"))
                    tipo = TipoTSP.SOP;
                  else if(valor.equals("HCP"))
                    tipo = TipoTSP.HCP;
                  else if(valor.equals("CVRP"))
                    tipo = TipoTSP.CVRP;
                  else if(valor.equals("TOUR"))
                    tipo = TipoTSP.TOUR;
                  else
                      throw new Exception("Tipo TSP inválido!");
                  
               }
               else if (palavraChave.equals("COMMENT"))
               {
                   comentario = valor;
               }
               else if(palavraChave.equals("DIMENSION"))
               {
                  dimensao = Integer.parseInt(valor);
               } 
               else if(palavraChave.equals("CAPACITY"))
               {
                  capacidade = Integer.parseInt(valor);
               } 
               else if(palavraChave.equals("EDGE_WEIGHT_TYPE"))
                {
                  if(valor.equals("EXPLICIT"))
                    tipoWeight = TipoWeight.EXPLICIT;
                  else if(valor.equals("EUC_2D"))
                    tipoWeight = TipoWeight.EUC_2D;
                  else if(valor.equals("EUC_3D"))
                    tipoWeight = TipoWeight.EUC_3D;
                  else if(valor.equals("MAX_2D"))
                    tipoWeight = TipoWeight.MAX_2D;
                  else if(valor.equals("MAX_3D"))
                    tipoWeight = TipoWeight.MAX_3D;
                  else if(valor.equals("MAN_2D"))
                    tipoWeight = TipoWeight.MAN_2D;
                  else if(valor.equals("MAN_3D"))
                    tipoWeight = TipoWeight.MAN_3D;
                  else if(valor.equals("CEIL_2D"))
                    tipoWeight = TipoWeight.CEIL_2D;
                  else if(valor.equals("GEO"))
                    tipoWeight = TipoWeight.GEO;
                  else if(valor.equals("ATT"))
                    tipoWeight = TipoWeight.ATT;
                  else if(valor.equals("XRAY1"))
                    tipoWeight = TipoWeight.XRAY1;
                  else if(valor.equals("XRAY2"))
                    tipoWeight = TipoWeight.XRAY2;
                  else
                    throw new Exception("Tipo WEIGHT inválido!");
                } 
                else if(palavraChave.equals("EDGE_WEIGHT_FORMAT")) 
                {
                  if (valor.equals("FUNCTION"))
                    tipoWeightFormat = TipoWeightFormat.FUNCTION;
                  else if(valor.equals("FULL_MATRIX"))
                    tipoWeightFormat = TipoWeightFormat.FULL_MATRIX;
                  else if(valor.equals("UPPER_ROW"))
                    tipoWeightFormat = TipoWeightFormat.UPPER_ROW;
                  else if(valor.equals("LOWER_ROW"))
                    tipoWeightFormat = TipoWeightFormat.LOWER_ROW;
                  else if(valor.equals("UPPER_DIAG_ROW"))
                    tipoWeightFormat = TipoWeightFormat.UPPER_DIAG_ROW;
                  else if(valor.equals("LOWER_DIAG_ROW"))
                    tipoWeightFormat = TipoWeightFormat.LOWER_DIAG_ROW;
                  else if(valor.equals("UPPER_COL"))
                    tipoWeightFormat = TipoWeightFormat.UPPER_COL;
                  else if(valor.equals("LOWER_COL"))
                    tipoWeightFormat = TipoWeightFormat.LOWER_COL;
                  else if(valor.equals("UPPER_DIAG_COL"))
                    tipoWeightFormat = TipoWeightFormat.UPPER_DIAG_COL;
                  else if(valor.equals("LOWER_DIAG_COL"))
                    tipoWeightFormat = TipoWeightFormat.LOWER_DIAG_COL;
                  else 
                  {
                    throw new Exception("EDGE_WEIGHT_FORMAT inválido!");
                  }
                }
                else if(palavraChave.equals("EDGE_DATA_FORMAT")) 
                {
                    if (valor.equals("EDGE_LIST"))
                        dataFormat = TipoDataFormat.EDGE_LIST;
                    else if(valor.equals("ADJ_LIST"))
                        dataFormat = TipoDataFormat.ADJ_LIST;
                }
                else if(palavraChave.equals("NODE_COORD_TYPE")) 
                {
                    if (valor.equals("TWOD_COORDS"))
                        nodeCoord = TipoNodeCoord.TWOD_COORDS;
                    else if(valor.equals("THREED_COORD"))
                        nodeCoord = TipoNodeCoord.THREED_COORD;
                    else if(valor.equals("NO_COORDS"))
                        nodeCoord = TipoNodeCoord.NO_COORDS;
                    else
                        nodeCoord = TipoNodeCoord.NO_COORDS;
                }
                else if(palavraChave.equals("DISPLAY_DATA_TYPE")) 
                {                    
                    if (valor.equals("TWOD_DISPLAY"))
                        displayData = TipoDisplayData.TWOD_DISPLAY;
                    else if(valor.equals("COORD_DISPLAY"))
                        displayData = TipoDisplayData.COORD_DISPLAY;
                    else if(valor.equals("NO_DISPLAY"))
                        displayData = TipoDisplayData.NO_DISPLAY;
                    else
                    {
                       if (nodeCoord != TipoNodeCoord.NO_COORDS)
                           displayData = TipoDisplayData.COORD_DISPLAY;
                       else
                           displayData = TipoDisplayData.NO_DISPLAY;
                    }
                }
                else if(palavraChave.equals("NODE_COORD_SECTION"))
                {
                    matriz      = new double[dimensao][dimensao];
                    coordenadas = new double[dimensao][3];
                    
                    int seq;
                    int k = 0;
                                        
                    for (int i=0; i < dimensao; i++)
                    {
                        linha   = in.readLine();
                        linha   = linha.trim();
                        
                        if ((tipoWeight == TipoWeight.EUC_2D) || (tipoWeight == TipoWeight.EUC_3D)) //((nodeCoord == TipoNodeCoord.THREED_COORD) || (nodeCoord == TipoNodeCoord.TWOD_COORDS))
                        {                            
                            posicao = linha.indexOf(" ");
                            seq     = Integer.parseInt(linha.substring(0, posicao));
                            linha   = linha.substring(posicao+1);     
                            
                            posicao = (linha.indexOf(" ")==(-1) ? linha.length() : linha.indexOf(" "));                               
                            coordenadas[seq-1][k++] = Math.round(Float.parseFloat(linha.substring(0, posicao)));                                
                            linha   = linha.substring(posicao+1);
                            
                            posicao = (linha.indexOf(" ")==(-1) ? linha.length() : linha.indexOf(" "));                               
                            coordenadas[seq-1][k++] = Math.round(Float.parseFloat(linha.substring(0, posicao)));                                                            
                            
                            if (tipoWeight == TipoWeight.EUC_3D)
                            {
                                linha   = linha.substring(posicao+1);
                                posicao = (linha.indexOf(" ")==(-1) ? linha.length() : linha.indexOf(" "));                               
                                coordenadas[seq-1][k++] = Float.parseFloat(linha.substring(0, posicao));                                
                                //linha   = linha.substring(posicao+1);
                            }
                            
                            System.out.println();
                            k = 0;   // reseta a variavel k
                        }
                        else if (tipoWeight == TipoWeight.GEO)
                        {
                            posicao = linha.indexOf(" ");
                            seq     = Integer.parseInt(linha.substring(0, posicao));                        
                            linha   = linha.substring(posicao+1);                        

                            posicao = (linha.indexOf(" ")==(-1) ? linha.length() : linha.indexOf(" "));
                            coordenadas[seq-1][k++] = Float.parseFloat(linha.substring(0, posicao));                                                        
                            linha   = linha.substring(posicao+1);
                                                        
                            posicao = (linha.indexOf(" ")==(-1) ? linha.length() : linha.indexOf(" "));
                            coordenadas[seq-1][k++] = Float.parseFloat(linha);                                                                                    
                            
                            k = 0;   // reseta a variavel k
                        }
                    }
                    
                    // monta a matriz de distancias
                    for (int i=0; i < dimensao; i++)
                        for (int j=0; j < dimensao; j++)
                        {
                            if (i==j)
                            {
                                matriz[i][j] = 0.0;
                            }
                            else
                            {
                                double distancia = calculaDistancia(tipoWeight, i,j);

                                matriz[i][j] = matriz[j][i] = distancia;
                                //matriz[j][i] = distancia;
                            }
                        }
                }                
                else if(palavraChave.equals("DEPOT_SECTION")) 
                {
                 
                }
                else if(palavraChave.equals("DEMAND_SECTION")) 
                {
                 
                } 
                else if(palavraChave.equals("EDGE_DATA_SECTION")) 
                {
                 
                }
                else if(palavraChave.equals("FIXED_EDGES_SECTION"))
                {
                    
                }
                else if(palavraChave.equals("DISPLAY_DATA_SECTION"))
                {
                    
                }
                else if(palavraChave.equals("TOUR_SECTION"))
                {
                    tour = new int[dimensao];
                    
                    for (int i=0; i < dimensao; i++)
                    {
                        linha   = in.readLine();                        
                        
                        tour[i] = Integer.parseInt(linha); 
                    }
                }
                else if(palavraChave.equals("EDGE_WEIGHT_SECTION"))
                {
                    
                }
            
            }

            in.close();
	} 
        catch (IOException e)
	{
            System.err.println("Erro na leitura do arquivo!");            
	}         
    }
    
    private double calculaDistancia(TipoWeight ewt, int indiceCidade1, int indiceCidade2) 
    {
        int distancia = 0;
        double xd, yd, zd = 0.0;
        double aux = 0.0;
        
        if ((tipoWeight == TipoWeight.EUC_2D) || (tipoWeight == TipoWeight.EUC_3D))
        {
            xd = (coordenadas[indiceCidade1][0] - coordenadas[indiceCidade2][0]);
            yd = (coordenadas[indiceCidade1][1] - coordenadas[indiceCidade2][1]);
            
            if (tipoWeight == TipoWeight.EUC_3D)
            {
                zd = (coordenadas[indiceCidade1][2] - coordenadas[indiceCidade2][2]);
            }
            
            aux = xd*xd + yd*yd + ((tipoWeight == TipoWeight.EUC_3D)?(zd*zd):0);
            
            distancia = (int) Math.sqrt(aux);
        }
        else if ((tipoWeight == TipoWeight.MAN_2D) || (tipoWeight == TipoWeight.MAN_3D))
        {
            xd = (coordenadas[indiceCidade1][0] - coordenadas[indiceCidade2][0]);
            yd = (coordenadas[indiceCidade1][1] - coordenadas[indiceCidade2][1]);
            
            if (tipoWeight == TipoWeight.MAN_3D)
            {
                zd = (coordenadas[indiceCidade1][2] - coordenadas[indiceCidade2][2]);
            }
            
            aux = xd + yd + ((tipoWeight == TipoWeight.MAN_3D)?zd:0);
            
            distancia = (int) aux;
        }
        else if ((tipoWeight == TipoWeight.MAX_2D) || (tipoWeight == TipoWeight.MAX_3D))
        {
            xd = (coordenadas[indiceCidade1][0] - coordenadas[indiceCidade2][0]);
            yd = (coordenadas[indiceCidade1][1] - coordenadas[indiceCidade2][1]);
            
            if (tipoWeight == TipoWeight.MAX_3D)
            {
                zd = (coordenadas[indiceCidade1][2] - coordenadas[indiceCidade2][2]);
                aux       = (int) Math.max((int) xd, (int) yd);                
                distancia = (int) Math.max( aux, (int) zd);
            }            
            else
            {
                distancia = Math.max( (int) xd, (int) yd);
            }           
            
        }
        else if (tipoWeight == TipoWeight.GEO)
        {
            // calcula latitude e longitude para a primeira cidade
            int grau   = (int) coordenadas[indiceCidade1][0];
            double minuto = coordenadas[indiceCidade1][0] - grau;            
            double latitude1 = PI * (grau + 5.0*minuto/3.0)/180.0;
            
            grau   = (int) coordenadas[indiceCidade1][1];
            minuto = coordenadas[indiceCidade1][1] - grau;            
            double longitude1 = PI * (grau + 5.0*minuto/3.0)/180.0;
            
            // calcula latitude e longitude para a segunda cidade
            grau   = (int) coordenadas[indiceCidade2][0];
            minuto = coordenadas[indiceCidade2][0] - grau;            
            double latitude2 = PI * (grau + 5.0*minuto/3.0)/180.0;
            
            grau   = (int) coordenadas[indiceCidade2][1];
            minuto = coordenadas[indiceCidade2][1] - grau;            
            double longitude2 = PI * (grau + 5.0*minuto/3.0)/180.0;
            
            double q1 = Math.cos( longitude1 - longitude2);
            double q2 = Math.cos( latitude1  - latitude2 );
            double q3 = Math.cos( latitude1  + latitude2 );
            
            distancia = (int) (RRR * Math.acos(0.5*((1.0 + q1)*q2 - (1.0 - q1)*q3)) + 1.0);
            
            
        }
        else if (tipoWeight == TipoWeight.ATT)
        {
            xd = (coordenadas[indiceCidade1][0] - coordenadas[indiceCidade2][0]);
            yd = (coordenadas[indiceCidade1][1] - coordenadas[indiceCidade2][1]);
            
            double rij = Math.sqrt((xd*xd + yd*yd) / 10.0);
            double tij = (int) rij;
            
            if (tij < rij)
                distancia = (int) (tij + 1);
            else
                distancia = (int) tij;
        }
        else if (tipoWeight == TipoWeight.CEIL_2D)
        {
            
        }
        else if ((tipoWeight == TipoWeight.XRAY1) || (tipoWeight == TipoWeight.XRAY2))
        {
            
        }
        
        return (distancia);
    }
    
    public String info()
    {
        String s = ""; 
        
        s = s + "TOUR \n";
        
        for (int i=0; i < (tour==null?0:tour.length); i++) 
        {
            s = s + this.tour[i] + "\n";            
        }
        
        s = s + "\n";
        s = s + "   ";
        
        for (int i = 0; i < (matriz==null?0:matriz[0].length); i++)
        {
            s = s + "\t"+ "["+i+"]";
        }
        s = s + "\n";
        
        for (int i = 0; i < (matriz==null?0:matriz[0].length); i++)
        {
            s = s + "["+ i + "]\t";
            for (int j = 0; j < (matriz==null?0:matriz[0].length); j++)
            {
                  s = s + matriz[i][j] + "\t";
            }
            s = s + "\n";
        }
        
        return (s);
    }
    
}
