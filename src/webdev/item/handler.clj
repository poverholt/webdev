(ns webdev.item.handler
  (:require [webdev.item.model-atom :refer [create-item
                                            read-items
                                            update-item
                                            delete-item]]
             [webdev.item.view :refer [items-page]]))

(def redirect-response {:status 302
                        :headers {"Location" "/items"}
                        :body ""})

(defn not-found-response [item-id-str]
  {:status 404
   :body (str "List not found for id: " item-id-str)
   :headers {}})
  
(defn handle-index-items [req]
  (let [;;db (:webdev/db req)
        items (read-items)]
    {:status 200
     :headers {}
     ;; :body (str "<html><head></head><body><div>"
     ;;            (mapv :name items)!
     ;;            "</div><form method=\"POST\" action=\"/items\">"
     ;;            "<input type=\"text\" name=\"name\" placeholder=\"name\">"
     ;;            "<input type=\"text\" name=\"description\" placeholder=\"description\">"
     ;;            "<input type=\"submit\">"
     ;;            "</body></html>")
     :body (items-page items)}))

(defn handle-create-item [req]
  (let [name (get-in req [:params "name"])
        description (get-in req [:params "description"])
        item-id (create-item name description)]
    redirect-response))

(defn handle-delete-item [req]
  (let [item-id-str (:item-id (:route-params req))
        item-id (java.util.UUID/fromString item-id-str)
        did-exist? (delete-item item-id)]
    (if did-exist?
      redirect-response
      (not-found-response item-id-str))))

(defn handle-update-item [req]
  (let [item-id-str (:item-id (:route-params req))
        item-id (java.util.UUID/fromString item-id-str)
        checked (get-in req [:params "checked"])
        exists? (update-item item-id (= "true" checked))]
    (if exists?
      redirect-response
      (not-found-response item-id-str))))
  
