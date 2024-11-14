describe('My Login application', () => {
    it('should login with valid credentials', async () => {
        const championCardDetail = await $(`android.widget.TextView[@text='Aatrox']`);
        await championCardDetail.waitForDisplayed({ timeout: 5000 });
        await championCardDetail.click();
        await browser.pause(3000);
    })
})

