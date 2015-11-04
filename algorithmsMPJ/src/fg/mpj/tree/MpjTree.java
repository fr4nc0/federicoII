package fg.mpj.tree;

import mpi.MPI;

public class MpjTree {

	public static void main(String[] args) throws Exception{

		/*
		 * il processo 0 è un dispatcher
		 * i processi da 1 a n ospitano i nodi
		 * ciascun processo può ospitare al max m nodi
		 */
		final int DISPATCHER 	= 0;
		final int ROOT 			= 1;
		
		MPI.Init(args);
		
		int me = MPI.COMM_WORLD.Rank();
		
		System.out.println("<" + me + "> started.");
		
		if ( me == DISPATCHER ) {
			
			/*
			 * invia a ROOT in numero punti da eseguire
			 */
			char [] message = "Hello, there".toCharArray() ;
			MPI.COMM_WORLD.Send(message, 0, message.length, MPI.CHAR, 1, 99);
			
		} else {
			
			/*
			 * riceve il messaggio con il num di punti da inserire
			 */
			char [] message = new char [20] ;
			MPI.COMM_WORLD.Recv(message, 0, 20, MPI.CHAR, 0, 99);
			
			/*
			 * estrae dal messaggio:
			 * 	 - il numero di punti da inserire (np)
			 * 	 - dal punto (da) al punto (a)
			 * 	 - la dept
			 * 	 - la dimensione del bucket
			 * 
			 * chiama load(np, da, a, dept, b)
			 */
		
			/*
			 * load
			 * 	ottiene la coordinata di split
			 * 	se il num di punti è inferiore a b
			 * 		se il num. di nodi processo < m
			 * 			crea un nuovo nodo n e inserisce i punti nel bucket
			 * 			set leaf = false, set coord, set height = 0
			 * 			ritorna n
			 * 		else 
			 * 			invia messaggio load a ?? 
			 * 			aspetta risposta
			 * 			ritorna ??
			 *  else
			 *  	se il num. di nodi processo < m
			 *  		trova la mediana dei punti da inserire
			 *  		crea un nuovo nodo n
			 *  		set leaf = false, set splitValue e split coordinate
			 *  		n.setLeft( load (prima metà punti...) )
			 *  		n.setRight( load (seconda metà punti...) )
			 *  		set height di n al max dei figli
			 *  		n.getLeft().setParent(n) e n.getRight().setParent(n) 
			 *  		ritorna n
			 *  	else 
			 *  		invia messaggio load a ??
			 *  		aspetta risposta
			 *  		ritorna ??
			 */
		}
		
		MPI.Finalize();
	}

}
