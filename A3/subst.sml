
datatype 'a ntree = leaf of 'a | node of 'a ntree list;

fun subst(tr, v1, v2) =

      let
        fun h (leaf(a)) = if a = v1 then leaf(v2) else leaf(a)
          | h (node(l)) = node((map h l))
      in
        h tr
      end


(*val res = subst(leaf(10),10,999); *)

val res = subst(node([leaf("x"), node([leaf("y"), leaf("x"), leaf("z")])]), "x", "a");
