(ns scramblies.handler
  (:require
    [clojure.walk :refer [keywordize-keys]]
    [reitit.ring :as reitit-ring]
    [scramblies.middleware :refer [middleware]]
    [hiccup.page :refer [include-js include-css html5]]
    [config.core :refer [env]]
    [scramblies.util :refer [scramble?]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to scramblies"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exciting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))


(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(defn scramble-handler
  [request]
  (try
    (let [{:keys [str1 str2]} (-> request (get :query-params) (keywordize-keys))
          result (scramble? str1 str2)]
      (when (or (nil? str1) (nil? str2))
        (throw (new Exception "Invalid input")))
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    (str result)})
    (catch Exception e
      {:status  500
       :headers {"Content-Type" "text/html"}
       :body    (.getMessage e)})))

(def app
  (reitit-ring/ring-handler
    (reitit-ring/router
      [["/" {:get {:handler index-handler}}]
       ["/scramble" {:get {:handler scramble-handler}}]
       ["/items"
        ["" {:get {:handler index-handler}}]
        ["/:item-id" {:get {:handler    index-handler
                            :parameters {:path {:item-id int?}}}}]]
       ["/about" {:get {:handler index-handler}}]])
    (reitit-ring/routes
      (reitit-ring/create-resource-handler {:path "/" :root "/public"})
      (reitit-ring/create-default-handler))
    {:middleware middleware}))
