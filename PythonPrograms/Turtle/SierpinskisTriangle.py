#Author: Joshua Abraham
#Name: Sierpinski's triangle
#Description: Using turtle, draws Sierpinski's triangle with user-determined side length.

import turtle 
import time

def middle_triangle(side_length):			#}
	for side in range(3):			#}
		Shaniqwa.left(60)			#} Function to draw one upside-down triangle (the middle triangle).
		Shaniqwa.forward(side_length)	#}
		Shaniqwa.left(60)			#}
	

def draw_sp_triangle (x, y, edge_len):
	
	half_edge_len = edge_len / 2					# <---- To calculate a half of the side length of the triangle.
	quarter_edge_len = half_edge_len / 2				# <---- To calculate a quarter of the side length of the triangle.
	half_height = (((edge_len)**2 - (half_edge_len)**2)**0.5) / 2	# <---- To calculate half of the perpendicular height of the triangle.	
	
	
	Shaniqwa.penup()					#}
	Shaniqwa.setposition (x + (half_edge_len), y)	#}
	Shaniqwa.pendown()				#} Draws the middle triangles.
							#}
	middle_triangle (half_edge_len)			#}	
	
	#Shaniqwa.stamp()

	if (half_edge_len > 7):	
		Shaniqwa.pencolor('green')		
		draw_sp_triangle (x, y, half_edge_len)	# sp_triangle function for left triangles.
	
	if (half_edge_len > 7):	
		Shaniqwa.pencolor('yellow')		
		draw_sp_triangle (x + quarter_edge_len, y + half_height, half_edge_len)	# sp_triangle function for top triangles.
	
	if (half_edge_len > 7):
		Shaniqwa.pencolor('red')			
		draw_sp_triangle (x + half_edge_len, y, half_edge_len)	# sp_triangle function for right triangles.


def draw_outside_triangle(x, y, edge_len):		#}
						#}
	Shaniqwa.penup()				#}
	Shaniqwa.setposition (x, y)		#}
	Shaniqwa.pendown()			#} Function to draw the border of the whole sp_triangle.
						#
	for side in range(3):			#
		Shaniqwa.forward(edge_len)	#
		Shaniqwa.left(120)		#

def full_sipernski (x, y, edge_len):		#}
						#} Combines inside sp_triangle itterations with outside border,
	draw_sp_triangle (x, y, edge_len)		#} to avoid drawing unnecesarry borders, thereby optimising speed, bro.
	draw_outside_triangle(x, y, edge_len)	#}

def goaway ():
	window.bye()


start = time.clock()

Shaniqwa = turtle.Turtle()
Shaniqwa.pencolor('green')
Shaniqwa.shape('classic')
Shaniqwa.speed(0)
Shaniqwa.pensize(0)

window = turtle.Screen()
window.title('''Sierpinski's Triangle, Bro.''')
window.bgcolor('white')

full_sipernski (-200, -((((400)**2 - (200)**2)**0.5) / 4), 400)

stop = time.clock()
print ("I just thought I'd let you know that I only took ", (stop-start), " seconds to draw this triangle, bro.")

window.onkey (goaway, 'q')
window.listen()
window.mainloop()


#--- Approximately 28 seconds for a triangle of size length 400
#---  and about 9 seconds for a triangle of size length 200.
#---  This is becau..






