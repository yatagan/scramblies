(ns scramblies.handler-test
  (:require [clojure.test :refer :all]
            [scramblies.handler :refer [app]]
            [ring.mock.request :as mock]))

(deftest scramble-handler-test
  (let [request-scramble (fn [str1 str2]
                           (let [{:keys [status body]} (app (mock/request :get "/scramble" {:str1 str1 :str2 str2}))]
                             {:status status :body body}))]
    (is (= (request-scramble "rekqodlw" "world") {:status 200 :body "true"}))
    (is (= (request-scramble "cedewaraaossoqqyt" "codewars") {:status 200 :body "true"}))
    (is (= (request-scramble "katas" "steak") {:status 200 :body "false"}))

    (is (= (-> (mock/request :get "/scramble")
               (app)
               (select-keys [:status])) {:status 500}))))
