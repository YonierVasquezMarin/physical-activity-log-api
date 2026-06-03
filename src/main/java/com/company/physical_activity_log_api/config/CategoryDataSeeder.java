package com.company.physical_activity_log_api.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.model.CategoryActivity;
import com.company.physical_activity_log_api.repository.CategoryActivityRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryDataSeeder implements ApplicationRunner {

	private final CategoryActivityRepository categoryActivityRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		for (DefaultCategoryDefinitions.DefaultCategory definition : DefaultCategoryDefinitions.CATEGORIES) {
			if (categoryActivityRepository.existsByUserIsNullAndName(definition.name())) {
				continue;
			}
			CategoryActivity category = new CategoryActivity();
			category.setUser(null);
			category.setName(definition.name());
			category.setDescription(definition.description());
			categoryActivityRepository.save(category);
		}
	}

}
