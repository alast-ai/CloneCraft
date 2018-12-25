#version 330 core

layout (location = 0) in vec2 in_position;

out vec2 texturecoords;

uniform mat4 u_model;

void main() {
	gl_Position = u_model * vec4(in_position, 0.0, 1.0);
	texturecoords = (in_position + 1.0) / 2.0;
}