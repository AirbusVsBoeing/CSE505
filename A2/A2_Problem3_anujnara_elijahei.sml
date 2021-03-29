fun f(1) =  1
    | f(n) = n*n  +  f(n-1)

fun f3 (1,k) = k
    | f3 (n,k) = f3(n-1, (n*n)+k);


fun f2 n = f3 (n,1);


fun flatten([])  = []
    | flatten(h::t)  = h @ flatten(t);

fun flat (acc, []) = acc
    | flat (acc, h::t) = flat(acc@h, t);


fun flatten2(l) = flat([],l);

datatype 'a tree = leaf of 'a | node of 'a tree * 'a tree;


fun cat(leaf(s)) = s | cat(node(t1,t2)) = cat(t1) ^ " " ^ cat(t2);

val tr = node(node(leaf("Go"),leaf("Java")),node(leaf("Python"),leaf("Scala")));


fun myconcat(leaf(s),es) = if es = "" then s else s ^ " " ^ es
    | myconcat(node(t1,t2),es) = myconcat(t1,myconcat(t2,es));

fun cat2(t) = myconcat(t,"");


f(5);
f2(5);

flatten([[1,2], [3,4,5], [], [6,7,8]]);
flatten2([[1,2], [3,4,5], [], [6,7,8]]);

cat(tr);
cat2(tr);
