% KEDARE HARSHVARDHAN 21B11198 

% The message signal is sin(2*pi*fm*t) ; fm=50 Hz
clear
echo on
t0=.2;                                 % signal duration (c)
ts=1/1000;                              % sampling interval (c)
fc=300;                                 % carrier frequency (c)
fs=1/ts;                                % sampling frequency 
t=[-1:ts:1];                            % time vector
df=0.3;                                 % desired frequency resolution
% message signal
fm = 50;                                % (c)
m = sin(2*pi*fm*t);                     % (c)
c=cos(2*pi*fc.*t);                      % carrier signal (c)
u=m.*c;                                 % modulated signal
y=u.*c;                             % mixing
[M,m,df1]=fftseq(m,ts,df);              % Fourier transform 
M=M/fs;                                 % scaling
[U,u,df1]=fftseq(u,ts,df);              % Fourier transform 
U=U/fs;                                 % scaling
[Y,y,df1]=fftseq(y,ts,df);              % Fourier transform
Y=Y/fs;                                 % scaling
f_cutoff=200;                           % cutoff freq. of the filter (c)
n_cutoff=floor(200/df1);                % Design the filter.
f=[0:df1:df1*(length(y)-1)]-fs/2;
H=zeros(size(f));                    
H(1:n_cutoff)=2*ones(1,n_cutoff);    
H(length(f)-n_cutoff+1:length(f))=2*ones(1,n_cutoff);
DEM=H.*Y;                   % spectrum of the filter output
dem=real(ifft(DEM))*fs;             % filter output
pause % Press a key to see the effect of mixing.
clf
subplot(3,1,1)
plot(f,fftshift(abs(M)))

title('Spectrum of the Message Signal')
xlabel('Frequency')
subplot(3,1,2)
plot(f,fftshift(abs(U)))
title('Spectrum of the Modulated Signal')
xlabel('Frequency')
subplot(3,1,3)
plot(f,fftshift(abs(Y)))
title('Spectrum of the Mixer Output')
xlabel('Frequency')
pause % Press a key to see the effect of filtering on the mixer output.
clf
subplot(3,1,1)
plot(f,fftshift(abs(Y)))
title('Spectrum of the Mixer Output')
xlabel('Frequency')
subplot(3,1,2)
plot(f,fftshift(abs(H)))
title('Lowpass Filter Characteristics')
xlabel('Frequency')
subplot(3,1,3)
plot(f,fftshift(abs(DEM)))
title('Spectrum of the Demodulator output')
xlabel('Frequency')
pause % Press a key to compare the spectra of the message and the received signal.
clf
subplot(2,1,1)
plot(f,fftshift(abs(M)))
title('Spectrum of the Message Signal')
xlabel('Frequency')
subplot(2,1,2)
plot(f,fftshift(abs(DEM)))
title('Spectrum of the Demodulator Output')
xlabel('Frequency')
pause % Press a key to see the message and the demodulator output signals.
subplot(2,1,1)
plot(t,m(1:length(t)))
title('The Message Signal')
xlabel('Time')
subplot(2,1,2)
plot(t,dem(1:length(t)))
title('The Demodulator Output')
xlabel('Time')