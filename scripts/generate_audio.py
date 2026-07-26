import wave
import struct
import math
import os

def generate_beep(filename, duration_ms, freq, volume=0.5):
    sample_rate = 44100
    num_samples = int(sample_rate * (duration_ms / 1000.0))

    with wave.open(filename, 'w') as wav_file:
        wav_file.setnchannels(1) # mono
        wav_file.setsampwidth(2) # 16-bit
        wav_file.setframerate(sample_rate)

        for i in range(num_samples):
            # Sine wave
            value = int(volume * 32767.0 * math.sin(2.0 * math.pi * freq * i / sample_rate))
            # Fade out
            fade_out = 1.0 - (i / num_samples)
            value = int(value * fade_out)
            data = struct.pack('<h', value)
            wav_file.writeframesraw(data)

def generate_click(filename):
    generate_beep(filename, 50, 1000, 0.3)

def generate_win(filename):
    # Upward arpeggio
    sample_rate = 44100
    duration_ms = 500
    num_samples = int(sample_rate * (duration_ms / 1000.0))

    with wave.open(filename, 'w') as wav_file:
        wav_file.setnchannels(1)
        wav_file.setsampwidth(2)
        wav_file.setframerate(sample_rate)

        for i in range(num_samples):
            progress = i / num_samples
            freq = 440 * (2 ** (progress * 2)) # Slide up 2 octaves
            value = int(0.5 * 32767.0 * math.sin(2.0 * math.pi * freq * i / sample_rate))
            fade = 1.0 - progress
            value = int(value * fade)
            data = struct.pack('<h', value)
            wav_file.writeframesraw(data)

def generate_lose(filename):
    # Downward arpeggio
    sample_rate = 44100
    duration_ms = 800
    num_samples = int(sample_rate * (duration_ms / 1000.0))

    with wave.open(filename, 'w') as wav_file:
        wav_file.setnchannels(1)
        wav_file.setsampwidth(2)
        wav_file.setframerate(sample_rate)

        for i in range(num_samples):
            progress = i / num_samples
            freq = 220 * (2 ** (-progress * 2)) # Slide down 2 octaves
            value = int(0.5 * 32767.0 * math.sin(2.0 * math.pi * freq * i / sample_rate))
            fade = 1.0 - progress
            value = int(value * fade)
            data = struct.pack('<h', value)
            wav_file.writeframesraw(data)

def generate_move(filename):
    generate_beep(filename, 80, 800, 0.2)

if __name__ == "__main__":
    # Define output directory relative to project root
    script_dir = os.path.dirname(os.path.abspath(__file__))
    project_root = os.path.dirname(script_dir)
    output_dir = os.path.join(project_root, "app", "src", "main", "res", "raw")

    os.makedirs(output_dir, exist_ok=True)

    generate_click(os.path.join(output_dir, "sfx_click.wav"))
    generate_win(os.path.join(output_dir, "sfx_win.wav"))
    generate_lose(os.path.join(output_dir, "sfx_lose.wav"))
    generate_move(os.path.join(output_dir, "sfx_move.wav"))
    print(f"Audio effects generated successfully in: {output_dir}")
