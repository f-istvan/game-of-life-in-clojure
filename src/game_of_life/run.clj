(ns game-of-life.run
  (:require [clojure.java.io :as io]))

;; rules:
;; 1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
;; 2) Any live cell with two or three live neighbours lives on to the next generation.
;; 3) Any live cell with more than three live neighbours dies, as if by overcrowding.
;; 4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

(defrecord Game [iteration-delay-in-millis
                 map-size
                 living-cells]

  Object
  (toString [_]
    (str "Iteration delay in millis: " iteration-delay-in-millis
         " Map size: " map-size
         " Living cells: " living-cells)))

(defn gonna-live?
  [cell]
  true)

(defn create-next-generation
  [game]
  (let [next-generation
        (for [cell (:living-cells game)
              :when (gonna-live? cell)]
          cell)]

    (Game.
          (:iteration-delay-in-millis game)
          (:map-size game)
          next-generation)))

(defn print-map
  [game]
  (println game))

(defn run-game
  [game]
  (let [delay (:iteration-delay-in-millis game)]
    (loop [game game]
        (print-map game)
        (Thread/sleep delay)
        (recur (create-next-generation game)))))

(defn test-fn
  "TODO remove this fn. A function for testing purpuses."
  [x]
  x)

(defn read-game-data-from-file
  [filename]
  (let [data-file (io/resource filename)
        init-state (read-string (slurp data-file))
        game (Game.
              (:iteration-delay-in-millis init-state)
              (:map-size init-state)
              (:living-cells init-state))]
    game))

(defn -main
  [& args]
  (let [game (read-game-data-from-file "init-world.txt")]
    (println "Starting Game Of Life...")
    (run-game game)))
