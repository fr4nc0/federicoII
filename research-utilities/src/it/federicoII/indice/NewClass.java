/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.federicoII.indice;

import java.io.*;
import java.util.ArrayList;
import net.vja2.research.util.FastMap;





/**
 *
 * @author Giancarlo
 */
public class NewClass {

    private double calculate_distance(ArrayList<String> concept, ArrayList<String> concept1, ArrayList<ArrayList<String>> tmp) {
        String common=this.getleastcommon(concept,concept1);
        Double numerator;
        int index=this.calculate_index(tmp, common);
        numerator=2*this.calculate_distance_node("ROOTESCO",tmp.get(index));
        Double denominator=this.calculate_distance_node("ROOTESCO", concept)+this.calculate_distance_node("ROOTESCO", concept1);
        if(Double.isNaN((numerator/denominator))){
            return 1;
        }
        else{
        return (numerator/denominator);}
    }
    
    public ArrayList<ArrayList<String>> readFile(String path, String filename){
         BufferedReader br = null;
         String sCurrentLine;
         String[] split;
         ArrayList<ArrayList<String>> tmp= new ArrayList<ArrayList<String>>();
         int index=0;
         try {
            br = new BufferedReader(new FileReader(path+"/"+filename));
 		  while ((sCurrentLine = br.readLine()) != null) {
                      System.out.println(index++);
                      tmp.add(new ArrayList<String>());
                      split=sCurrentLine.split(";");
                      for(int i=0;i<split.length;i++){
                          if(!split[i].isEmpty()){
                              tmp.get(tmp.size()-1).add(split[i]);
                          }
                      }
                  }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
                    return tmp;
    }
    
     private String getleastcommon(ArrayList<String> concept, ArrayList<String> concept1) {
         String s = null;
         boolean found = false;
         for(int i=1;i<concept.size();i++){
             if(found==false){
             for(int j=1;j<concept1.size();j++){
                 if(concept.get(i).equals(concept1.get(j))){
                     s=concept.get(i);
                     found=true;
                     break;
                 }
             }}
         }
         return s;
     }
    
    private double calculate_distance_node(String node, ArrayList<String> get) {
         double d=0;
          for(int i=1;i<get.size();i++){
              if(get.get(i).equals(node)){
               d=i;   
              }
          }
         return (d-1);
    }
     
       private int calculate_index(ArrayList<ArrayList<String>> tmp, String common) {
        int index=-1;
        boolean found=false;
           for(int i=0;i<tmp.size();i++){
            if(found==false){
            if(tmp.get(i).get(0).contains(common)){
                index=i;
                found=true;
            }}
        }
           return index;
    }
       
       private void write_distance_matrix(ArrayList<ArrayList<Double>> distance, ArrayList<ArrayList<String>> tmp, String path) {
        BufferedWriter wr = null;
         String tmp2;
         String tmp1="Rows";
          try {
            wr = new BufferedWriter(new FileWriter(path+"/Distance_matrix.txt"));
             for(int i=0;i<tmp.size();i++){
                 System.out.println("***********************************");
                 System.out.println(i);
                 tmp1=tmp1+";"+tmp.get(i).get(0);
             }
              wr.write(tmp1+"\n");
              for(int i=0;i<distance.size();i++){
                   System.out.println("***********************************");
                 System.out.println(i);
                  tmp2=tmp.get(i).get(0);
                  for(int j=0;j<distance.get(i).size();j++){
                          tmp2=tmp2+";"+distance.get(i).get(j).toString();
                  }
                  wr.write(tmp2+"\n");
              }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (wr != null)wr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }
       
        private void write_point(String path, String filename, FastMap.DataMapping<RDFtriplet> map) {
         BufferedWriter wr = null;
         String tmp;
          try {
            wr = new BufferedWriter(new FileWriter(path+"/"+filename));
 		 for(FastMap.MapPoint<RDFtriplet> mp : map.mappeddata) {
                      System.out.println("***********************************");
                      System.out.println(mp.data().getId());
                  wr.write(mp.data().getObject()+";"+mp.map[0] + ";" + mp.map[1] + ";" + mp.map[2]+"\n"); }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (wr != null)wr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }
     
    public static void main(String[] args)  {
      NewClass nc=new NewClass();
        ArrayList<ArrayList<String>> tmp = nc.readFile("C:/Users/Giancarlo/Desktop", "analisi.txt");
       // ArrayList<ArrayList<ArrayList<String>>> distance=new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<Double>> distance=new ArrayList<ArrayList<Double>>();
        int dimensions=3;
        for(int i=0;i<tmp.size();i++){
            //distance.add(new ArrayList<ArrayList<String>>());
            distance.add(new ArrayList<Double>());
            System.out.println(i);
            for(int j=0;j<tmp.size();j++){
             /*distance.get(i).add(new ArrayList<String>());
             distance.get(i).get(distance.get(i).size()-1).add(tmp.get(i).get(0));
             distance.get(i).get(distance.get(i).size()-1).add(tmp.get(j).get(0));        
             */distance.get(i).add(1-nc.calculate_distance(tmp.get(i), tmp.get(j),tmp));
                     //get(distance.get(i).size()-1).add(String.valueOf(1-nc.calculate_distance(tmp.get(i), tmp.get(j),tmp)));
            }
        }
        nc.write_distance_matrix(distance,tmp,"C:/Users/Giancarlo/Desktop");
        System.out.println("File Scritto");
        ArrayList<RDFtriplet> list_classes=new ArrayList<RDFtriplet>();
        for(int i=0;i<tmp.size();i++){
          System.out.println(i);
          list_classes.add(new RDFtriplet(String.valueOf(list_classes.size()),String.valueOf(list_classes.size()), "Azienda", "Cerca", tmp.get(i).get(0)));
        }
       DistanceImpl distance_impl = new DistanceImpl(distance,tmp);
        FastMap<RDFtriplet> mapper = new FastMap<RDFtriplet>(list_classes, distance_impl);
        FastMap.DataMapping<RDFtriplet> map = mapper.map(dimensions);
        nc.write_point("C:/Users/Giancarlo/Desktop", "Fastmap_point.txt",map);
    }     
}