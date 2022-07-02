
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class YapayZeka {

    Queue<Integer> queue_start = new LinkedList();
    Queue<Integer> queue_destination = new LinkedList();
    
    

    int graph_matrix[][];
    int cities;// hedef sayısı
    int roads;// yol sayısı
    String[] line ;
    int k;
  
    
    boolean[] visited_start;// ziyaret edilen başlangıç başlatmak için 
    boolean[] visited_destination;//hedef düğüm tutulması için 
    
    String city_list[];// düğüm isimlerini tutan dizi
    String start;//düğümü başlatmak için
    String destination;//hedef düğüm adı
    
    int parent_start[];//başlangıç için ebeveynin isimlerini tutan dizi
    int parent_destination[];//bisonraki için ebevynin kaydının tutulması

    String visited_node_for_start[];//ziyaret edilen düğümü tutan dizi
    String visited_node_for_desti[];//çarpışma olup olmadığını kontrol eden dizi
    
    int sCounter = 0;
    int dCounter = 0;
    
    int cityValueToAssigned;//şehir değeri atama sayacı
    String collisionCity;// çarpışılan şehir
    
    //Grafiğin ayarlanması
    public void setGraph() throws Exception {
        FileReader fileReader = new FileReader("sample.txt");// okunan dosya
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String stringLine = bufferedReader.readLine();//köşe ve kenar uzunluklarını aldık
        String lineArray[] = stringLine.split(", ");// dosyadaki nereden bölüneceğini söylüyor köşe ve kenar sayısını diziye atıyoruz
        cities = Integer.parseInt(lineArray[0]);// birinci şehiri al
        roads = Integer.parseInt(lineArray[1]);// varış şehir
        
        line = new String[roads];

        //başlatma
        
        graph_matrix = new int[cities][cities];// düğüm sayısı
        
        city_list = new String[cities];// dosyadaki düğüm isminin atanması Kenar uzunluğunun adresini alma
       
        visited_start = new boolean[cities];// ziyeret edilen düğğüm dizitye atanıyor boyutlar atanıyor
        
        visited_destination = new boolean[cities];// hedef düğüm diziye atanıyor
        
        parent_start = new int[cities];//ebeveyn atanıyor
        parent_destination = new int[cities];// bir sonraki ebeveyin atanıyor

        visited_node_for_start = new String[cities];// ziyaret edilen düğüm atanıyor
        visited_node_for_desti = new String[cities];// çarpışma
        // boyut atamaları 

        //Başlangıç ve hedef düğümün girilmesi
        start = bufferedReader.readLine();// Dosyadan başlangıç düğümü değişkene atanır
        destination = bufferedReader.readLine();//Gidilecek düğüm değişkene atanıyor
        System.out.println("Başlangıç:"+start+" \n"+"Varış:"+destination);
        cityValueToAssigned = 0;// şehir değeri
        for (int i = 0; i < roads; i++) {// yol sayısı kadarıyla dön
            stringLine = bufferedReader.readLine();// Her bir yolun satır satır okunması
            lineArray = stringLine.split(", ");// yolun adresleri alınır
            
            line[i]=stringLine;
         
            //System.out.println(stringLine.charAt(6)+"------------"+line[i].charAt(6));

            int row = cityValueToAssigned;// satır 
            int cityValue = checkCityValueAssigned(lineArray[0]);//şehre atanmış değeri döndürüyor

            if (cityValue < 0) {
                city_list[cityValueToAssigned] = lineArray[0];
                cityValueToAssigned++;
                k=line[i].charAt(6);
                
            } else {
                row = cityValue;
            }

            int col = cityValueToAssigned;// sutun
            cityValue = checkCityValueAssigned(lineArray[1]);

            if (cityValue < 0) {
                city_list[cityValueToAssigned] = lineArray[1];
                cityValueToAssigned++;
                k=line[i].charAt(6);
            } else {
                col = cityValue;
            }

            graph_matrix[row][col] = 1;
            graph_matrix[col][row] = 1;
            
        }
        
    }
  

    public void Bidirectional_Search() {
        int startLoc = checkCityValueAssigned(start);
        int destiLoc = checkCityValueAssigned(destination);// hedef düğüm 

        queue_start.add(startLoc);// sıradaki
        visited_start[startLoc] = true;// ziyaret edilen başlangıç

        queue_destination.add(destiLoc);// ziyeret edilenler dizisine eklenerek 
        visited_destination[destiLoc] = true;// ziyaret edildiğinini göstermek için true yapıldı

        visited_node_for_start[0] = start;
        visited_node_for_desti[0] = destination;

        Bidirectional_Search_visit();
        if (collisionCity != null) {// çarpışma boş değilse 
            printShortestPath();
        } else {
            System.out.println("Doğrudan rota bulunamadı");
        }

    }

    public void Bidirectional_Search_visit() {
        boolean collisionChecker = false;// çarpışmayı kontrol edilen değişken

        while (!queue_start.isEmpty() && !queue_destination.isEmpty() && !collisionChecker) {//dizi boş değilse
            int fromQ_start = queue_start.poll();// kuyruğun başındaki ögeyi alarak kuyruktan siler
            int fromQ_desti = queue_destination.poll();// hedf düğümüm alır ve kuyruktan siler

            for (int i = 0; i < cities; i++) {// şehir sayısınca döndür
                if (!visited_start[i] & graph_matrix[fromQ_start][i] > 0) {
                    queue_start.add(i);// ziyeret edilmemişse kuyruğa ekle
                    //System.out.println( "kökten çarpışmaya"+ " " + city_list[i] + " " + i);//******************************
                    visited_start[i] = true;// ziyaret edildi olarak işaretle
                    parent_start[i] = fromQ_start;// ebeveyni ata
                    visited_node_for_start[sCounter++] = city_list[i];
                }
            }
            for (int i = 0; i < cities; i++) {
                if (!visited_destination[i] & graph_matrix[fromQ_desti][i] > 0) {
                    queue_destination.add(i);
                    //System.out.println("hedeften çarpışmaya" + " " + city_list[i] + " " + i);//****************************************
                    visited_destination[i] = true;
                    parent_destination[i] = fromQ_desti;

                    visited_node_for_desti[dCounter++] = city_list[i];
                }
            }
            //düğümlerin komşu yollar bulunduktan sonra, çarpışmayı kontrol edelim
            for (int i = 0; i < sCounter; i++) {
                for (int j = 0; j < dCounter; j++) {
                    if (visited_node_for_start[i] != null && visited_node_for_desti[j] != null) {//ziyaret edilenler boş değilse ve 
                        if (visited_node_for_start[i].equals(visited_node_for_desti[j])) {// iki taraftan kontrol edilen ebeveyinler eşit ise
                            System.out.println("çarpışma bulundu = " + visited_node_for_start[i]);//*************************
                            collisionChecker = true;// çarpışma var collisionChecker
                            collisionCity = visited_node_for_start[i];
                            break;
                        }
                    }
                    if (collisionChecker) {
                        break;
                    }
                }
            }
        }
    }

    //alınan şehir isminin önceden bir değer ile atanmış olup olmadığını kontrol et eğer atanmamaiş işe -1 döndür
    public int checkCityValueAssigned(String city_name) {
        for (int i = 0; i < cityValueToAssigned; i++) {
            if (city_list[i] != null) {
                if (city_list[i].equals(city_name)) {
                    
                    return i;
                }
            }
        }
        return -1;
    }
    
   //en kısa yolu çarpışma alnını ve yol numarasını önden başlayarak çarpışmaya kadar yazdır
    public void printShortestPath() {
        int collisionVal = 0;// çarpışma değer
        int length = 0;// uzunluk

        for (int i = 0; i < cities; i++) {//çarpışma düğümün değerinin bul
            if (city_list[i].equals(collisionCity)) {
                //System.out.println(city_list[i]+"--"+line[i].charAt(6));
                //line[i].charAt(6);
                collisionVal = i;// şehrin değerini inderxsini al
                break;
            }
        }
        int destinationVal = 0;// hedef
        for (int i = 0; i < cities; i++) {//hedef şehir değerini bul
            if (city_list[i].equals(destination)) {
                destinationVal = i;// hedef değeri döndür
                break;
            }
        }

        int cVal = collisionVal;// çarpışma

        String box[] = new String[cities];

        int i = 0;
        int t=0;
        cVal = collisionVal;//ters baskı çarpışma indexsi baştan
        while (cVal != 0) {
            box[i] = city_list[cVal];// diziye çarpışmayı ata
            cVal = parent_start[cVal];
            t +=line[i].charAt(6);
            //System.out.println("toplam:"+t);
            i++;
        }
        box[i] = city_list[0];

        System.out.print("Route:Kök= ");
        for (int j = i; j >= 0; j--) {//baştan çarpışmaya kadar yazdır 
            System.out.print(box[j] + "->");
            length++;
        }
        while (collisionVal != destinationVal) {//çarpışmadan hedefe yazdır

            collisionVal = parent_destination[collisionVal];
            length++;

            if (collisionVal == destinationVal) {
                System.out.println(city_list[collisionVal]);
            } else {
                System.out.print(city_list[collisionVal] + "->");
            }

        }
        System.out.println("Length: " + (length - 1));
        System.out.println("Yolların çarpışmaya başladığı rota : " + collisionCity+" #Roads(İki yönle ilerlenmesi ile birlikte mesafe): "+(i));
       
    }
    
    public Integer[] prediction_function() throws FileNotFoundException, IOException{
        int a = 0;
        Integer adizi[] = null;
        File file = new File("sample3.txt");
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        
        String satir = reader.readLine();
        int i =0;
            while (satir!=null) {
                satir = reader.readLine();
                String greedy[] = satir.split(", ");
                i++;
                if(i>=3){
                    a=satir.charAt(6);
                    adizi[i]=a;
                    
               
                }
                
            }
        
      return adizi;  
    }
    public  void a() throws IOException{
        Integer[] TahminiFonksiyon;
        TahminiFonksiyon = prediction_function();
        for(Integer a:TahminiFonksiyon){
            System.out.println(a);
        }
    }

    public void print() {
        System.out.println("şehirlere atanan değerler :");
        for (int i = 0; i < cities; i++) {
            System.out.println(city_list[i] + " " + i);
        }
//        System.out.println("grap_matrix values:");
//        for (int i = 0; i < cities; i++) {
//            for (int j = 0; j < cities; j++) {
//                System.out.print(graph_matrix[i][j]);
//            }
//            System.out.println();
//        }
    }

}
