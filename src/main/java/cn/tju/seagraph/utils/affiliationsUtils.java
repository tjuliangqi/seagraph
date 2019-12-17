package cn.tju.seagraph.utils;

import java.util.Arrays;

public class affiliationsUtils {
    public static String strToaffiliations(String name){
        String affiliation = null;
//        System.out.println(name);
        if (name.contains(",")){
            String[] Affiliations = name.split(",");
            if (name.toLowerCase().contains("university")){
                for(String affil:Affiliations) {
                    if (affil.toLowerCase().contains("university")) {
                        affiliation = affil;
                        return affiliation;
                    }
                }
            }else if(name.toLowerCase().contains("institute")){
                for(String affil:Affiliations) {
                    if (affil.toLowerCase().contains("institute")){
                        affiliation = affil;
                        return affiliation;
                    }
                }
            }
            else if(name.toLowerCase().contains("school")){
                for(String affil:Affiliations) {
                    if (affil.toLowerCase().contains("school")){
                        affiliation = affil;
                        return affiliation;
                    }
                }
            }
            else {
                if(Affiliations[0].length()<=2){
                    affiliation = null;
                }else {

                    affiliation = Affiliations[0];
                }
            }


        }else{
            affiliation=name;
        }
        return affiliation;
    }
}
