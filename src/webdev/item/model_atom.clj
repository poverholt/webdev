(ns webdev.item.model-atom
  (:import [java.util UUID]))

(def table (atom {}))

(defn create-item [name description]
  (let [id (UUID/randomUUID)]
    (swap! table #(assoc % id {:id id :name name :description description :checked false}))
    id))

(defn update-item [id checked]
  (swap! table #(assoc-in % [id :checked] checked)))

(defn delete-item [id]
  (let [exists? (id @table)]
    (when exists? (swap! table #(dissoc % id)))
    exists?))

(defn read-items []
  (vals @table))

;;(create-item "Pete" "Smarty pants")
;;(create-item "Bucky" "Sweety pie")
;;(read-items)
;;(def id1 (first (keys  @table)))
;;(update-item id1 true)
;;(delete-item id1)

