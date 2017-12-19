(ns game-of-life.run
  (:require [clojure.java.io :as io]))

;; rules:
;; 1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
;; 2) Any live cell with two or three live neighbours lives on to the next generation.
;; 3) Any live cell with more than three live neighbours dies, as if by overcrowding.
;; 4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

(defn print-map
  [game]
  (println game))

(defrecord Game
    [iteration-delay-in-millis
     map-size
     living-cells]

  Object
  (toString [_]
    (str "Iteration delay in millis: " iteration-delay-in-millis
         " Map size: " map-size
         " Living cells: " living-cells)))

(defn get-all-surrounding-cells
  [cell]
  (let [cell-x (get cell 0)
        cell-y (get cell 1)]
    (for [x (range (dec cell-x) (+ cell-x 2))
          y (range (dec cell-y) (+ cell-y 2))]
      [x y])))

(defn count-duplicates
  [a b]
  (let [concatd (concat a b)]
    (- (count concatd) (count (set concatd)))))

(defn living-gonna-live?
  [number-of-living-neighbours]
  (cond
    ;; 1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
    (< number-of-living-neighbours 2) false

    ;; 2) Any live cell with two or three live neighbours lives on to the next generation.
    (or (== number-of-living-neighbours 2) (== number-of-living-neighbours 3)) true

    ;; 3) Any live cell with more than three live neighbours dies, as if by overcrowding.
    (> number-of-living-neighbours 3) false))

(defn dead-gonna-live?
  [number-of-living-neighbours]
  ;; 4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
  (if (== number-of-living-neighbours 3) true false))

(defn gonna-live?
  [cell living-cells]
  (let [all-surrounding-cells (get-all-surrounding-cells cell)
        number-of-living-neighbours (dec (count-duplicates all-surrounding-cells living-cells))
        is-a-living-cell (count-duplicates living-cells [cell])]

    (if is-a-living-cell
      (living-gonna-live? number-of-living-neighbours)
      (dead-gonna-live? number-of-living-neighbours))))

(defn get-all-cells
  [map-size]
  (for [x (range (:x map-size))
        y (range (:y map-size))]
    [x y]))

(defn create-next-generation
  [game]
  (let [next-generation
        (for [cell (get-all-cells (:map-size game))
              :when (gonna-live? cell (:living-cells game))]
          cell)]

    (doall next-generation)
    ;; (print-map game)

    (Game.
          (:iteration-delay-in-millis game)
          (:map-size game)
          next-generation)))

(defn run-game
  [game]
  (let [delay (:iteration-delay-in-millis game)]
    (loop [game game]
      (Thread/sleep delay)
      (recur (create-next-generation game)))))

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
