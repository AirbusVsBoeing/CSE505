datatype 'a tree = leaf of 'a | node of 'a tree * 'a tree;

fun cat_cps(leaf(a),K) = K (a)
  | cat_cps(node(t1,t2),K) = cat_cps(t1, fn r => (r^" "^(cat_cps(t2,K))));

fun cat2(tr) = cat_cps(tr, (fn x => x))

val tr = node(node(leaf("Go"), leaf("Java")), node(leaf("Python"), leaf("Scala")));
(*val test = node(node(node(leaf("Anuj"),leaf("Elijah")),leaf("CSE")),node(leaf("UB"),leaf("505"))); *)

(*val pas = node(leaf("Hello"), leaf("world!")); *)
cat2(tr);
