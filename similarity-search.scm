;; Function: Read a histogram from a file
;; Input: String filename
;; Output: List of histogram values
(define (read-histogram filename)
  (with-input-from-file filename
    (lambda ()
      (let reading ((lines '()))
        (let ((line (read-line)))
          (if (eof-object? line)
              (reverse lines)
              (reading (cons (string->number line) lines))))))))

;; Function: Calculate the intersection between two histograms
;; Input: Two lists of histogram values
;; Output: Histogram intersection value
(define (histogram-intersection hist1 hist2)
  (define (helper hist1 hist2 sum)
    (if (or (null? hist1) (null? hist2))
        sum
        (helper (cdr hist1) 
                (cdr hist2) 
                (+ sum (min (car hist1) (car hist2))))))
  (helper hist1 hist2 0))


;; Function: Process a single image's histogram, compare with the query histogram, and maintain a sorted list of top 5
;; Input: A list of tuples containing histogram and filename, the query histogram, and the list of top matches
;; Output: Updated list of top 5 matches
(define (process-image histograms query-histogram top-matches)
  (if (null? histograms)
      top-matches
      (let* ((current (car histograms))
             (current-histogram (read-histogram (car current)))
             (similarity (histogram-intersection current-histogram query-histogram))
             (updated-matches (insert-sorted top-matches (cons similarity (cdr current)))))
        (process-image (cdr histograms) query-histogram (take-top-5 updated-matches)))))

;; Function: Insert a new match into the sorted list of top matches
;; Input: Sorted list of matches, new match
;; Output: Updated sorted list of matches
(define (insert-sorted matches match)
  (if (or (null? matches) (> (car match) (caar matches)))
      (cons match matches)
      (cons (car matches) (insert-sorted (cdr matches) match))))

;; Function: Keep only the top 5 matches
;; Input: List of matches
;; Output: List of top 5 matches
(define (take-top-5 matches)
  (if (> (length matches) 5)
      (take-top-5 (cdr matches))
      matches))

;; Function: The main function to find most similar images based on histogram
;; Input: Query histogram filename, image dataset directory
;; Output: List of names of the top 5 most similar images
(define (similaritySearch queryHistogramFilename imageDatasetDirectory)
  (let* ((query-histogram (read-histogram queryHistogramFilename))
         (image-files (list-directory imageDatasetDirectory)) ;; You'll need to provide list-directory function
         (image-histograms (map (lambda (file) (cons (string-append imageDatasetDirectory "/" file ".hist") file)) image-files))
         (top-matches (process-image image-histograms query-histogram '())))
    (map cdr top-matches))) ;; Extract filenames from the matches

;; Dummy function: List files in a directory
;; Input: Directory path
;; Output: List of file names (as strings)
(define (list-directory path)
  ;; This function is platform-dependent and not defined in standard Scheme.
  ;; You would need to either pass this list to your Scheme function or
  ;; use an external command like `ls` or `find` to get this list.
  '("image1" "image2" "image3" "image4" "image5"))

;; NOTE: The above code assumes you have a mechanism in place to list the files in the image dataset directory.
;; Scheme itself does not have this capability, so you would need to handle this in another way, 
;; possibly by calling an external command or by passing the list of files to your Scheme program.
