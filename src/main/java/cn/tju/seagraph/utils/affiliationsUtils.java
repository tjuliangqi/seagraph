package cn.tju.seagraph.utils;

public class affiliationsUtils {
    public static String strToaffiliations(String name){
        String affiliation = null;
        if (name.contains(",")){
            String[] Affiliations = name.split(",");
            for(String affil:Affiliations){
                if (affil.contains("university")){
                    affiliation = affil;
                }else {
                    if (Affiliations.length!=0 && Affiliations.length>=2){
                        affiliation = Affiliations[1];
                    }else if (Affiliations.length == 1){
                        affiliation = Affiliations[0];
                    }else {
                        affiliation = null;
                    }
                }
            }

        }
        return affiliation;
    }
}
