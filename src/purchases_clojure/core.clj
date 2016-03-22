(ns purchases-clojure.core 
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))

(defn -main []
  (println "Enter a Category: Furniture, Alcohol, Toiletries, Shoes, Food, Jewelry")
  (println "Or Type q to Exit")
  (loop []
        (let [  category (read-line)
                purchases (slurp "purchases.csv")
                purchases (str/split-lines purchases)
                purchases (map (fn [line]
                                 (str/split line #","))
                            purchases)
                header (first purchases)
                purchases (rest purchases)
                purchases (map (fn [line]
                                 (apply hash-map (interleave header line)))
                             purchases)
                purchases (walk/keywordize-keys purchases)
                purchases (filter (fn [line]
                                    (= (:category line) category))
                            purchases)]
          (when (not= category "q")
              (println purchases)
              (spit "filtered_purchases.edn" (pr-str purchases))
              (println "Enter a Category: Furniture, Alcohol, Toiletries, Shoes, Food, Jewelry")
              (println "Or Type q to Exit")
              (recur)))))
      
      
      
        
    
        
  
        
        
                                    
        
     
