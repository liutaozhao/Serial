int configure_port1(int fd , speed_t st)
{
   struct termios port_settings;	  // structure to store the port settings in

   //获得当前设备模式，与终端相关的参数。fd=0标准输入
   tcgetattr(fd, &port_settings);
   //设置结构termios输入波特率为115200Bps
   cfsetispeed(&port_settings, st);    // set baud rates
   //设置输出波特率
   cfsetospeed(&port_settings, st);
 
   port_settings.c_cflag &= ~PARENB;	// set no parity, stop bits, data bits
   port_settings.c_cflag &= ~CSTOPB;
   port_settings.c_cflag &= ~CSIZE;
   port_settings.c_cflag |= CS8;
   port_settings.c_cflag |= (CLOCAL | CREAD);
   port_settings.c_lflag &= (~(ECHO | ECHOE));
//	 port_settings.c_lflag |= (ECHO | ECHOE);
//	 port_settings.c_lflag |= (ICANON);
//	 port_settings.c_lflag &= (~ICANON);
//   port_settings.c_lflag &=  (~(ICANON | ECHO | ECHOE | ECHOK | ISIG | IEXTEN));
   port_settings.c_lflag &= (~ICANON);
//   port_settings.c_lflag = 0;
   port_settings.c_iflag = IGNPAR;
/*
  port_settings.c_oflag &= ~OPOST;
    for (int i=0 ; i<NCCS; i++) {
	printf("c_cc[%d] = %x\n", i, port_settings.c_cc[i]);
		
   }
  
   port_settings.c_cc[VMIN] = 1;

   port_settings.c_cc[VTIME] = 0; */
    int i = tcflush(fd, TCIFLUSH);
   //设置终端参数，TCSANOW修改立即生效
   int k = tcsetattr(fd, TCSANOW, &port_settings);	  // apply the settings to the port
   printf("\n------>(i:%d,k:%d,fd:%d)\n",i,k,fd);
   return(fd);
} 

int open_port1(int nUartNo)
{
   int fd; // file description for the serial port
        int i;
        char text[512] = "hello";
        char buf[256];
        int res;
        if(nUartNo==0)
        {
                fd = open("/dev/ttyS0", O_RDWR | O_NOCTTY);
                printf("liutao debug::open ttyS0\n");
        }
        else if(nUartNo==1)
        {
                fd = open("/dev/ttyS1", O_RDWR | O_NOCTTY/*| O_NONBLOCK*/);
                printf("liutao debug::open ttyS1\n");
        }
        else
                fd= -1;
   if(fd == -1) // if open is unsucessful
   {
          perror("open_port: Unable to open /dev/ttyS0 - ");
   }
   else
   {
        //      fcntl(fd, F_SETFL, O_NONBLOCK);
        //if(nUartNo==0)
                configure_port1(fd, B115200);
                //else
                //configure_port1(fd, B57600);
   }
   return(fd);
}

void rs232_uart_ctrl( bool bDebugOn)
{
     if(bDebugOn)// switch to RS232 command mode,bDebugOn==true do this
     {
               /* if(m_fd1==-1)
                        m_fd1 = open_port1(1);
                m_fd=m_fd1;*/
		open_port1(1);
                //printf("yuanlian debug:: m_fd = %d,start to set register\n",m_fd);
                //sleep(10);
		IoReg_Mask32(0xb806020C, 0xFF0FFFFF, 0x00700000);
                IoReg_Mask32(0xb806020C, 0xFFFF0FFF, 0x00007000);
                rtd_maskl(0xb80008a8, ~(_BIT13), 0x0);
     }
     else//switch to normal uart log mode,bDebugOn==false do this
     {


                //if(m_fd0==-1)
                       // m_fd0 = open_port1(0);
                //m_fd=m_fd0;
				open_port1(0);
                //printf("yuanlian debug:: m_fd = %d,start to set register\n",m_fd);
                IoReg_Mask32(0xb806020C, 0xFF0FFFFF, 0x00600000);
                IoReg_Mask32(0xb806020C, 0xFFFF0FFF, 0x00006000);
                rtd_maskl(0xb80008a8, ~(_BIT13), 0x1);
     }
}