import java.util.Scanner;
public class Solution
{

 public static void main(String[] args)
 {
  //code to read values 
  Football[] fball=new Football[4];
  Scanner sc=new Scanner(System.in);
  for(int i=0;i<fball.length;i++){
      int playerId=sc.nextInt(); sc.nextLine();
      String playerName=sc.nextLine();
      String country=sc.nextLine();
      int goals=sc.nextInt();
      fball[i]=new Football(playerId, playerName, country, goals);
  }
  String country=sc.nextLine();
  String country1=sc.nextLine();
  
  //code to call required method
  int sum=findGoalsforCountry(fball, country);
      if(sum>0){
          System.out.println(sum);
      }
      else{
      System.out.println("No valid Country specified");
      }
 }
  
  Football fb=getGoalsInAscendingOrder(fball, country);
  
  if(fb != null){
      System.out.println(fb.getplayerId()+"#"+fb.getplayerName()+"#"+fb.getcountry()+"#"+fb.getgoals());
  }
  else{
      System.out.println("Country specified is wrong");
  }
  
   
 

public static int  findGoalsforCountry(Football[] fball,String country)
  {
   //method logic
   int sum=0;
   for(int i=0;i<fball.length;i++){
       if(fball[i].getcountry().equalsIgnoreCase(country)){
           sum=sum+fball[i].getgoals();
           sum++;
       }
   }
   if(sum==0){
       return 0;
   }
   
   
  }
public static Football[] getGoalsInAscendingOrder(Football[] fball, String country)
  {
   //method logic
   Football[] fb=null;
   for(int i =0;i<fball.length;i++){
       if(fball[i].getcountry()==country){
           fb=fball[i];
           break;
       }
       else{
           return fb;
       }
   }
   
  }
}
class Football
{
  //code to build the class
  private int playerId;
  private String playerName;
  private String country;
  private int goals;
  
  public Football(int playerId, String playerName, String country, int goals){
      super();
      this.playerId=playerId;
      this.playerName=playerName;
      this.country=country;
      this.goals=goals;
  }
  //GettersAndSetters
  
  public int getplayerId(){
      return playerId;
  }
      public void setplayerId(int playerId){
          this.playerId=playerId;
      }
      public String getplayerName(){
          return playerName;
      }
      public void setplayerName(String playerName){
          this.playerName=playerName;
      }
      public String getcountry(){
          return country;
      }
      public void setcountry(String country){
          this.country=country;
      }
      public int getgoals(){
          return goals;
      }
      public void setgoals(int goals){
          this.goals=goals;
      }
  }
