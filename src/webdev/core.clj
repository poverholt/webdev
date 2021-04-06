(ns webdev.core
  (:require [webdev.item.model :as items]
            [webdev.item.handler :as handler])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(def db "jdbc:postgresql://localhost/webdev?user=pete&password=HIDDEN") ;; Use Machine password
;; This worked to get past authentication, but got error "This ResultSet is closed" later.

(defn greet [req]
  {:status 200
   :body "Hello Superman"
   :header {}})

(defn about [req]
  {:status 200
   :body "Superman saves innocent victims."
   :header {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, Cruel World!"
   :header {}})

(defn yo [req]
  {:status 200
   :body (str "Hello " (-> req :route-params :name) "!")
   :header {}})

(def operators {"+" +, "-" -, "*" *, ":" /})
  
(defn calc [req]
  (do
    (println "In Calc")
    (let [a (Integer/parseInt (get-in req [:route-params :a]))
          op (get-in req [:route-params :op])
          f (get operators op)
          b (Integer/parseInt (get-in req [:route-params :b]))]
      (if f
        {:status 200
         :body (str a " " op " " b " is " (f a b))
         :header {}}
        {:status 404
         :body (str "Unknown operator: " op)
         :header {}}))))

(defroutes routes
  (GET "/" [] greet)
  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (GET "/items" [] handler/handle-index-items)
  (GET "/goodbye" [] goodbye)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)
  (not-found "Page not found."))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Listronica 9000")))

(def app
  (wrap-server
   (wrap-params
    routes)))

(defn -main [port]
  ;; (items/create-table db)
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  ;; (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
