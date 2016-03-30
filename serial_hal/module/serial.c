#include <hardware/hardware.h>
#define LOG_TAG "serialdriver"
#include <fcntl.h>
#include <termios.h>
#include <errno.h>
#include <cutils/log.h>
#include <cutils/atomic.h>
#include <sys/ioctl.h>
#include <errno.h>
#include <string.h>
#include <dirent.h>
#include "../include/serial.h"

int fd;

static int serial_device_close(struct hw_device_t* device)
{
	ALOGE("%s E", __func__);
	struct serial_control_device_t* ctx = (struct serial_control_device_t*)device;
	if (ctx) {
		free(ctx);
	}
	close(fd);
	ALOGE("%s X", __func__);
	return 0; 
}

static int serial_read_drv(struct serial_control_device_t *dev, char *buf, int count)
{	
	ALOGE("%s E", __func__);
	int len = 0;
	len = read(fd, buf, count);
	if(len < 0)
	{
		perror("read");
	}
	ALOGE("%s X", __func__);
	return len;
}

static int serial_write_drv(struct serial_control_device_t *dev, char *buf, int size)
{	
	ALOGE("%s E", __func__);
	int len = write(fd, buf, size);
	if(len < 0)
	{
		perror("write");
	}
	ALOGE("%s X", __func__);
	return len;
}

static int serial_device_open(const struct hw_module_t* module, const char* name,
		struct hw_device_t** device)
{
	ALOGE("%s E", __func__);
	struct serial_control_device_t *dev;
	struct termios opt; 

	dev = (struct serial_control_device_t *)malloc(sizeof(*dev));
	memset(dev, 0, sizeof(*dev)); 

	//HAL must init property
	dev->common.tag= HARDWARE_DEVICE_TAG; 
	dev->common.version = 0;
	dev->common.module= module;

	dev->serial_read_hal = serial_read_drv;

	dev->serial_write_hal = serial_write_drv;
	*device= &dev->common;

	// MichaelTang add for open ttyUSBx 
	char devname[PATH_MAX];
	DIR *dir;
	struct dirent *de;
	dir = opendir("/dev");
	if(dir == NULL)
		return -1;
	strcpy(devname, "/dev");
	char * filename = devname + strlen(devname);
	*filename++ = '/';
	while((de = readdir(dir))) {
		if(de->d_name[0] == '.' || strncmp(de->d_name, "ttyS0", 5))        // start with . will ignor
			continue;
		strcpy(filename, de->d_name);
		if((fd = open(devname, O_RDWR | O_NOCTTY )) < 0)
		{       
			ALOGE("open error,fd=%d",fd);
			return -1;
		}else {
			ALOGE("open ok\n");
			break;
		}
	}


	struct termios port_settings;      // structure to store the port settings in
 
   tcgetattr(fd, &port_settings);
   cfsetispeed(&port_settings, B115200);    // set baud rates
   cfsetospeed(&port_settings, B115200);
 
   port_settings.c_cflag &= ~PARENB;    // set no parity, stop bits, data bits
   port_settings.c_cflag &= ~CSTOPB;
   port_settings.c_cflag &= ~CSIZE;
   port_settings.c_cflag |= CS8;
   port_settings.c_cflag |= (CLOCAL | CREAD);
   port_settings.c_lflag &= (~(ECHO | ECHOE));
//   port_settings.c_lflag |= (ECHO | ECHOE);
//   port_settings.c_lflag |= (ICANON);
   port_settings.c_lflag &= (~ICANON);
   port_settings.c_iflag = IGNPAR;
   int i = tcflush(fd, TCIFLUSH);
   int k = tcsetattr(fd, TCSANOW, &port_settings);    // apply the settings to the port

	ALOGE("%s X", __func__);

	return 0;
}

static struct hw_module_methods_t serial_module_methods = {
open: serial_device_open  
};


struct serial_module_t HAL_MODULE_INFO_SYM = {
common: {
tag: HARDWARE_MODULE_TAG,
     version_major: 1,
     version_minor: 0,
     id: serial_HARDWARE_MODULE_ID, 
     name: "serial HAL module",
     author: "farsight",
     methods: &serial_module_methods, 
	}, 
	/* supporting APIs go here */
};



