/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui;

/**
 *
 * @author user
 */
public class LoremIpsum {
    public static String getLine(){
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Pellentesque eu turpis augue. Aenean sodales fermentum venenatis. Nullam eget nisl id augue tempus tincidunt sed in magna. "
                + "Sed ac nisi nulla. Nunc semper nisi sit amet tempor iaculis. Donec a metus commodo, vulputate enim in, eleifend diam. "
                + "Duis auctor sapien sollicitudin eros ornare semper. Duis mollis mattis justo, a tristique arcu congue ut. Nullam blandit consequat fermentum. "
                + "In sodales non libero in tristique. Proin vitae tortor libero. Cras quis elit quis eros ullamcorper convallis id ut erat.";
    }
    
    public static String getLine(int words){
        String[] allWords = getLine().split(" ");

        if (words < 1) words = 1;
        if (words > allWords.length) words = allWords.length;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words; i++) {
            if (i > 0) result.append(" ");
            result.append(allWords[i]);
        }
        return result.toString();
    }
    
    public static String getParagraphs(){
        return """
               Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eu turpis augue. Aenean sodales fermentum venenatis. Nullam eget nisl id augue tempus tincidunt sed in magna. Sed ac nisi nulla. Nunc semper nisi sit amet tempor iaculis. Donec a metus commodo, vulputate enim in, eleifend diam. Duis auctor sapien sollicitudin eros ornare semper. Duis mollis mattis justo, a tristique arcu congue ut. Nullam blandit consequat fermentum. In sodales non libero in tristique. Proin vitae tortor libero. Cras quis elit quis eros ullamcorper convallis id ut erat.
               
               In id consectetur dui. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tincidunt augue feugiat nulla porta, at congue mauris pellentesque. Nulla dictum nunc quis eros tristique, et sagittis lacus scelerisque. Integer consectetur quis magna ac porta. Phasellus faucibus augue vitae tincidunt placerat. Nullam imperdiet, nibh nec semper elementum, felis risus consectetur quam, ut vulputate elit felis id arcu.
               
               Curabitur quis massa volutpat nisi molestie porta. Sed tincidunt turpis vel augue congue imperdiet. Quisque maximus, magna facilisis gravida ullamcorper, dui urna sagittis risus, et efficitur quam est at nulla. Vivamus pellentesque condimentum mi eu sollicitudin. Suspendisse vestibulum tristique tincidunt. Ut et maximus libero, ac pulvinar lacus. In hac habitasse platea dictumst. Fusce dolor justo, tincidunt sed consectetur a, placerat ac lacus. Quisque euismod, enim vitae fermentum aliquam, felis ipsum fringilla nisl, at porttitor dui leo tincidunt nulla.
               
               Duis mattis metus nec orci pulvinar, vitae varius ex rhoncus. Quisque porta tempor nisi, ut malesuada sem condimentum eget. Ut quis lacus erat. Praesent blandit ex libero, sit amet porttitor ante malesuada et. Quisque pulvinar iaculis eros. Donec egestas diam vitae pellentesque eleifend. Sed nunc ante, lacinia vitae vestibulum et, ullamcorper et metus. Duis congue diam a massa gravida, id vestibulum tellus mattis. Mauris sed tellus mi. Praesent maximus tempus risus, id fringilla neque porttitor ac. Cras at dictum nibh. Curabitur mattis elit sit amet odio lacinia euismod. Curabitur aliquam, turpis in gravida ullamcorper, ipsum quam efficitur nulla, ut porttitor nisl lacus ac massa.
               
               Vivamus facilisis nibh eros, at blandit arcu sagittis non. Nunc eros dui, mollis hendrerit massa ac, blandit pellentesque nulla. Aliquam erat volutpat. Vivamus eu cursus diam. Vivamus ultricies dui suscipit facilisis tincidunt. Pellentesque posuere vulputate mollis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed mattis pellentesque ante ac convallis. Aliquam finibus, arcu nec ultricies tincidunt, nulla dolor lacinia nibh, vel feugiat tellus turpis lobortis purus. Maecenas molestie eros nec ultricies molestie. Sed eleifend orci sed enim aliquam, a auctor nibh fringilla. Vivamus massa urna, vestibulum quis efficitur eget, consectetur ut sapien. In hendrerit erat imperdiet ante feugiat finibus. Nullam condimentum, diam nec posuere congue, justo tellus pharetra urna, non elementum turpis risus ut diam. Curabitur lobortis tortor in nulla faucibus, ut mollis nisi cursus.
               """;
    }
}
