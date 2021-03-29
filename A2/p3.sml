datatype 'a tree = leaf of 'a | node of 'a tree * 'a tree;


fun cat(leaf(s)) = s | cat(node(t1,t2)) = cat(t1) ^ " " ^ cat(t2);

val tr = node(node(leaf("Go"),leaf("Java")),node(leaf("Python"),leaf("Scala")));

val tn = node(node(leaf("Anuj"),leaf("from")),node(node(leaf("Chennai"),leaf("is")),node(leaf("a"),leaf("bodybuilder!"))));


fun myconcat(leaf(s),es) = if es = "" then s else s ^ " " ^ es
    | myconcat(node(t1,t2),es) = myconcat(t1,myconcat(t2,es));

fun cat2(t) = myconcat(t,"");

cat(tn);
cat2(tn);
