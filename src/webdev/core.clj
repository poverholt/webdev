(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))

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

(defroutes app
  (GET "/" [] greet)
  (GET "/about" [] about)
  (GET "/goodbye" [] goodbye)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
