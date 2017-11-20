(ns game-of-life.run
  (:require [clojure.java.io :as io]))

;; rules:
;; 1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
;; 2) Any live cell with two or three live neighbours lives on to the next generation.
;; 3) Any live cell with more than three live neighbours dies, as if by overcrowding.
;; 4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

;; useful functions
;; assoc-in
;; get-in

(defn start-game
  "Entry point"
  []
  (println "Starting Game Of Life...")
  (let [data-file (io/resource "init-world.txt")]
    (println (slurp data-file))))

(defn test-fn
  "A function for testing purpuses"
  [x]
  x)

(defn -main
  [& args]
  (start-game))
