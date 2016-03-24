(ns purchases-clojure.core 
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [ring.middleware.params :as p]
            [hiccup.core :as h])
  (:gen-class))

(def purchases
        (let [  purchases (slurp "purchases.csv")
                purchases (str/split-lines purchases)
                purchases (map (fn [line]
                                 (str/split line #","))
                            purchases)
                header (first purchases)
                purchases (map (fn [line]
                                 (apply hash-map (interleave header line)))
                             purchases)
                purchases (walk/keywordize-keys purchases)]
          purchases))

(def header (vals (first purchases)))

(def categories (vec (set (map (fn [purchase]
                                (:category purchase))
                            (rest purchases)))))

(defn filtered-purchases [category] (filter (fn [line]
                                              (= (:category line) category))
                                      (rest purchases)))

(defn categories-html []
  [:div
    (map (fn [category]
            [:span
              [:a {:href (str "/?category=" category)} category]
              " "])
      categories)])

(defn purchases-html [purchases]
  [:ol
    (map (fn [purchase]
            [:li (str/join " " (vals purchase))])
        (rest purchases))])

(c/defroutes app
  (c/GET "/" request
    (let [params (:params request) 
          category (get params "category") 
          category (or category "Alcohol")
          purchases (filtered-purchases category)]
        (h/html [:html
                  [:body
                    (categories-html)
                    (purchases-html (rest purchases))]]))))
                    
(defn -main []
  (j/run-jetty (p/wrap-params app) {:port 3000}))  
      
      
      
        
    
        
  
        
        
                                    
        
     
