module Setoid

%default total
%access public export

Relation : Type -> Type
Relation a = a -> a -> Type

RelationProperty : Type -> Type
RelationProperty a = Relation a -> Type

Trans : {A: Type} -> RelationProperty A
Trans {A=typeA} rel = (a: typeA) -> (b: typeA) -> (c: typeA) -> rel a b -> rel b c -> rel a c

Symm : {A: Type} -> RelationProperty A
Symm {A=typeA} rel = (a: typeA) -> (b: typeA) -> rel a b -> rel b a

Rfl : {A: Type} -> RelationProperty A
Rfl {A=typeA} rel = (a: typeA) -> rel a a

refl_eq : Rfl (=)
refl_eq _ = Refl

symm_eq : Symm (=)
symm_eq _ _ = sym

trans_eq : Trans (=)
trans_eq _ _  _ = trans

interface VerifiedEquality a (e : Relation a) where
    refl  : Rfl e
    symm  : Symm e
    trans : Trans e

[intensional] VerifiedEquality a (=) where
    refl = refl_eq
    symm = symm_eq
    trans = trans_eq
