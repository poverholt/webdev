(ns webdev.core
  (:require [webdev.item.model :as items]
            [webdev.item.handler :as handler])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
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
  (ANY "/request" [] handle-dump)
  (GET "/items" [] handler/handle-index-items)
  (POST "/items" [] handler/handle-create-item)
  (DELETE "/items/:item-id" [] handler/handle-delete-item)
  (GET "/goodbye" [] goodbye)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)
  (not-found "Page not found."))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Listronica 9000")))

(defn wrap-simulated-methods [hdlr]
  (let [sim-methods {"PUT" :put
                     "DELETE" :delete}]
    (fn [req]
      (if-let [method (and (= :post (:request-method req))
                           (sim-methods (get-in req [:params "_method"])))]
        (hdlr (assoc req :request-method method))
        (hdlr req)))))

(def app
  (wrap-server
   (wrap-file-info
    (wrap-resource
     (wrap-params
      (wrap-simulated-methods
       routes))
     "static"))))

(defn -main [port]
  ;; (items/create-table db)
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  ;; (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
