g x = x * x
f x = x * (g x)
main = do
       print $ f 2
       print (g (f (g 5)))
