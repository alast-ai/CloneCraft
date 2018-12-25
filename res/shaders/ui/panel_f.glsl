#version 330 core

in vec2 texturecoords;

layout (location = 0) out vec4 out_color;

uniform vec4 u_color;

void main() {
	out_color = u_color;
}
