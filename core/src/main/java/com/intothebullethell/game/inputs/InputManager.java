package com.intothebullethell.game.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {
    private boolean pausaSolicitada;

    private boolean up, down, left, right, recargar, disparar;
    private boolean upJustReleased, downJustReleased, leftJustReleased, rightJustReleased, disparandoJustReleased;

    public void setPausaSolicitada(boolean pausaSolicitada) {
        this.pausaSolicitada = pausaSolicitada;
    }

    public boolean isPausaSolicitada() {
        return pausaSolicitada;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                up = true;
                upJustReleased = false;
                break;
            case Keys.A:
                left = true;
                leftJustReleased = false;
                break;
            case Keys.D:
                right = true;
                rightJustReleased = false;
                break;
            case Keys.S:
                down = true;
                downJustReleased = false;
                break;
            case Keys.R:
                recargar = true;
                break;
            case Keys.ESCAPE:
                pausaSolicitada = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                up = false;
                upJustReleased = true;
                break;
            case Keys.A:
                left = false;
                leftJustReleased = true;
                break;
            case Keys.D:
                right = false;
                rightJustReleased = true;
                break;
            case Keys.S:
                down = false;
                downJustReleased = true;
                break;
            case Keys.R:
                recargar = false;
                break;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            disparar = true;
            disparandoJustReleased = false;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            disparar = false;
            disparandoJustReleased = true;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public boolean isUpJustReleased() {
        if (upJustReleased) {
            upJustReleased = false;
            return true;
        }
        return false;
    }

    public boolean isDownJustReleased() {
        if (downJustReleased) {
            downJustReleased = false;
            return true;
        }
        return false;
    }

    public boolean isLeftJustReleased() {
        if (leftJustReleased) {
            leftJustReleased = false;
            return true;
        }
        return false;
    }

    public boolean isRightJustReleased() {
        if (rightJustReleased) {
            rightJustReleased = false;
            return true;
        }
        return false;
    }

    public boolean isUp() { return up; }
    public boolean isDown() { return down; }
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isRecargar() { return recargar; }
    public boolean isDisparar() { return disparar; }

    public boolean isDisparandoJustReleased() {
        if (disparandoJustReleased) {
            disparandoJustReleased = false;
            return true;
        }
        return false;
    }
	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}
}
