#import "@preview/diagram:0.4.1": diagram, node, edge

#set text(font: ("Linux Libertine", "Arial", sans), size: 10pt)
#set page(margin: 1.5cm)

= Блок-схемы методов численного интегрирования

#let base-style = (
  node: (shape: "rect", fill: white, stroke: black, radius: 2pt),
  edge: (stroke: black, arrowhead: "stealth", label-style: (fill: white)),
  graph: (layout: "dot"),
)

== 1. Метод трапеций (`trapezoid`)
#diagram(
  base-style,
  node("t_start", "Начало", shape: "circle"),
  node("t_h", "h = (b - a) / n", shape: "rect"),
  node("t_init", "sum = (f(a) + f(b)) / 2", shape: "rect"),
  node("t_i", "i = 0", shape: "rect"),
  node("t_cond", "i < n ?", shape: "diamond"),
  node("t_body", "sum += f(a + i * h)", shape: "rect"),
  node("t_inc", "i++", shape: "rect"),
  node("t_res", "return h * sum", shape: "rect"),
  node("t_end", "Конец", shape: "circle"),
  edge("t_start", "t_h"),
  edge("t_h", "t_init"),
  edge("t_init", "t_i"),
  edge("t_i", "t_cond"),
  edge("t_cond", "t_body", label: "Да"),
  edge("t_body", "t_inc"),
  edge("t_inc", "t_cond"),
  edge("t_cond", "t_res", label: "Нет"),
  edge("t_res", "t_end"),
)
